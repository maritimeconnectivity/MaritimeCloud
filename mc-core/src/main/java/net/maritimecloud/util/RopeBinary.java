/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package net.maritimecloud.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Class to represent {@code Binarys} formed by concatenation of other Binarys, without copying the data in the pieces.
 * The concatenation is represented as a tree whose leaf nodes are each a {@link LiteralBinary}.
 *
 * <p>
 * Most of the operation here is inspired by the now-famous paper <a
 * href="http://www.cs.ubc.ca/local/reading/proceedings/spe91-95/spe/vol25/issue12/spe986.pdf"> BAP95 </a> Ropes: an
 * Alternative to Strings hans-j. boehm, russ atkinson and michael plass
 *
 * <p>
 * Fundamentally the Rope algorithm represents the collection of pieces as a binary tree. BAP95 uses a Fibonacci bound
 * relating depth to a minimum sequence length, sequences that are too short relative to their depth cause a tree
 * rebalance. More precisely, a tree of depth d is "balanced" in the terminology of BAP95 if its length is at least
 * F(d+2), where F(n) is the n-the Fibonacci number. Thus for depths 0, 1, 2, 3, 4, 5,... we have minimum lengths 1, 2,
 * 3, 5, 8, 13,...
 *
 * @author carlanton@google.com (Carl Haverl)
 */
class RopeBinary extends Binary {

    /**
     * BAP95. Let Fn be the nth Fibonacci number. A {@link RopeBinary} of depth n is "balanced", i.e flat enough, if its
     * length is at least Fn+2, e.g. a "balanced" {@link RopeBinary} of depth 1 must have length at least 2, of depth 4
     * must have length >= 8, etc.
     *
     * <p>
     * There's nothing special about using the Fibonacci numbers for this, but they are a reasonable sequence for
     * encapsulating the idea that we are OK with longer strings being encoded in deeper binary trees.
     *
     * <p>
     * For 32-bit integers, this array has length 46.
     */
    static final int[] MIN_LENGTH_BY_DEPTH;

    static {
        // Dynamically generate the list of Fibonacci numbers the first time this
        // class is accessed.
        List<Integer> numbers = new ArrayList<>();

        // we skip the first Fibonacci number (1). So instead of: 1 1 2 3 5 8 ...
        // we have: 1 2 3 5 8 ...
        int f1 = 1;
        int f2 = 1;

        // get all the values until we roll over.
        while (f2 > 0) {
            numbers.add(f2);
            int temp = f1 + f2;
            f1 = f2;
            f2 = temp;
        }

        // we include this here so that we can index this array to [x + 1] in the
        // loops below.
        numbers.add(Integer.MAX_VALUE);
        MIN_LENGTH_BY_DEPTH = new int[numbers.size()];
        for (int i = 0; i < MIN_LENGTH_BY_DEPTH.length; i++) {
            // unbox all the values
            MIN_LENGTH_BY_DEPTH[i] = numbers.get(i);
        }
    }

    private final int totalLength;

    final Binary left;

    final Binary right;

    private final int leftLength;

    private final int treeDepth;

    /**
     * Create a new RopeBinary, which can be thought of as a new tree node, by recording references to the two given
     * strings.
     *
     * @param left
     *            string on the left of this node, should have {@code size() >
     *              0}
     * @param right
     *            string on the right of this node, should have {@code size() >
     *              0}
     */
    RopeBinary(Binary left, Binary right) {
        this.left = left;
        this.right = right;
        leftLength = left.size();
        totalLength = leftLength + right.size();
        treeDepth = Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1;
    }

    /**
     * Concatenate the given strings while performing various optimizations to slow the growth rate of tree depth and
     * tree node count. The result is either a {@link LiteralBinary} or a {@link RopeBinary} depending on which
     * optimizations, if any, were applied.
     *
     * <p>
     * Small pieces of length less than {@link Binary#CONCATENATE_BY_COPY_SIZE} may be copied by value here, as in
     * BAP95. Large pieces are referenced without copy.
     *
     * @param left
     *            string on the left
     * @param right
     *            string on the right
     * @return concatenation representing the same sequence as the given strings
     */
    static Binary concatenate(Binary left, Binary right) {
        Binary result;
        RopeBinary leftRope = left instanceof RopeBinary ? (RopeBinary) left : null;
        if (right.size() == 0) {
            result = left;
        } else if (left.size() == 0) {
            result = right;
        } else {
            int newLength = left.size() + right.size();
            if (newLength < Binary.CONCATENATE_BY_COPY_SIZE) {
                // Optimization from BAP95: For short (leaves in paper, but just short
                // here) total length, do a copy of data to a new leaf.
                result = concatenateBytes(left, right);
            } else if (leftRope != null && leftRope.right.size() + right.size() < CONCATENATE_BY_COPY_SIZE) {
                // Optimization from BAP95: As an optimization of the case where the
                // Binary is constructed by repeated concatenate, recognize the case
                // where a short string is concatenated to a left-hand node whose
                // right-hand branch is short. In the paper this applies to leaves, but
                // we just look at the length here. This has the advantage of shedding
                // references to unneeded data when substrings have been taken.
                //
                // When we recognize this case, we do a copy of the data and create a
                // new parent node so that the depth of the result is the same as the
                // given left tree.
                Binary newRight = concatenateBytes(leftRope.right, right);
                result = new RopeBinary(leftRope.left, newRight);
            } else if (leftRope != null && leftRope.left.getTreeDepth() > leftRope.right.getTreeDepth()
                    && leftRope.getTreeDepth() > right.getTreeDepth()) {
                // Typically for concatenate-built strings the left-side is deeper than
                // the right. This is our final attempt to concatenate without
                // increasing the tree depth. We'll redo the the node on the RHS. This
                // is yet another optimization for building the string by repeatedly
                // concatenating on the right.
                Binary newRight = new RopeBinary(leftRope.right, right);
                result = new RopeBinary(leftRope.left, newRight);
            } else {
                // Fine, we'll add a node and increase the tree depth--unless we
                // rebalance ;^)
                int newDepth = Math.max(left.getTreeDepth(), right.getTreeDepth()) + 1;
                if (newLength >= MIN_LENGTH_BY_DEPTH[newDepth]) {
                    // The tree is shallow enough, so don't rebalance
                    result = new RopeBinary(left, right);
                } else {
                    result = new Balancer().balance(left, right);
                }
            }
        }
        return result;
    }

    /**
     * Concatenates two strings by copying data values. This is called in a few cases in order to reduce the growth of
     * the number of tree nodes.
     *
     * @param left
     *            string on the left
     * @param right
     *            string on the right
     * @return string formed by copying data bytes
     */
    private static LiteralBinary concatenateBytes(Binary left, Binary right) {
        int leftSize = left.size();
        int rightSize = right.size();
        byte[] bytes = new byte[leftSize + rightSize];
        left.copyTo(bytes, 0, 0, leftSize);
        right.copyTo(bytes, 0, leftSize, rightSize);
        return new LiteralBinary(bytes); // Constructor wraps bytes
    }

    /**
     * Create a new RopeBinary for testing only while bypassing all the defenses of {@link #concatenate(Binary, Binary)}
     * . This allows testing trees of specific structure. We are also able to insert empty leaves, though these are
     * dis-allowed, so that we can make sure the implementation can withstand their presence.
     *
     * @param left
     *            string on the left of this node
     * @param right
     *            string on the right of this node
     * @return an unsafe instance for testing only
     */
    static RopeBinary newInstanceForTest(Binary left, Binary right) {
        return new RopeBinary(left, right);
    }

    /**
     * Gets the byte at the given index. Throws {@link ArrayIndexOutOfBoundsException} for backwards-compatibility
     * reasons although it would more properly be {@link IndexOutOfBoundsException}.
     *
     * @param index
     *            index of byte
     * @return the value
     * @throws ArrayIndexOutOfBoundsException
     *             {@code index} is < 0 or >= size
     */
    @Override
    public byte byteAt(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index < 0: " + index);
        }
        if (index > totalLength) {
            throw new ArrayIndexOutOfBoundsException("Index > length: " + index + ", " + totalLength);
        }

        byte result;
        // Find the relevant piece by recursive descent
        if (index < leftLength) {
            result = left.byteAt(index);
        } else {
            result = right.byteAt(index - leftLength);
        }
        return result;
    }

    @Override
    public int size() {
        return totalLength;
    }

    // =================================================================
    // Pieces

    @Override
    protected int getTreeDepth() {
        return treeDepth;
    }

    /**
     * Determines if the tree is balanced according to BAP95, which means the tree is flat-enough with respect to the
     * bounds. Note that this definition of balanced is one where sub-trees of balanced trees are not necessarily
     * balanced.
     *
     * @return true if the tree is balanced
     */
    @Override
    protected boolean isBalanced() {
        return totalLength >= MIN_LENGTH_BY_DEPTH[treeDepth];
    }

    /**
     * Takes a substring of this one. This involves recursive descent along the left and right edges of the substring,
     * and referencing any wholly contained segments in between. Any leaf nodes entirely uninvolved in the substring
     * will not be referenced by the substring.
     *
     * <p>
     * Substrings of {@code length < 2} should result in at most a single recursive call chain, terminating at a leaf
     * node. Thus the result will be a {@link LiteralBinary}. {@link #RopeBinary(Binary, Binary)}.
     *
     * @param beginIndex
     *            start at this index
     * @param endIndex
     *            the last character is the one before this index
     * @return substring leaf node or tree
     */
    @Override
    public Binary substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Beginning index: " + beginIndex + " < 0");
        }
        if (endIndex > totalLength) {
            throw new IndexOutOfBoundsException("End index: " + endIndex + " > " + totalLength);
        }
        int substringLength = endIndex - beginIndex;
        if (substringLength < 0) {
            throw new IndexOutOfBoundsException("Beginning index larger than ending index: " + beginIndex + ", "
                    + endIndex);
        }

        Binary result;
        if (substringLength == 0) {
            // Empty substring
            result = Binary.EMPTY;
        } else if (substringLength == totalLength) {
            // The whole string
            result = this;
        } else {
            // Proper substring
            if (endIndex <= leftLength) {
                // Substring on the left
                result = left.substring(beginIndex, endIndex);
            } else if (beginIndex >= leftLength) {
                // Substring on the right
                result = right.substring(beginIndex - leftLength, endIndex - leftLength);
            } else {
                // Split substring
                Binary leftSub = left.substring(beginIndex);
                Binary rightSub = right.substring(0, endIndex - leftLength);
                // Intentionally not rebalancing, since in many cases these two
                // substrings will already be less deep than the top-level
                // RopeBinary we're taking a substring of.
                result = new RopeBinary(leftSub, rightSub);
            }
        }
        return result;
    }

    // =================================================================
    // Binary -> byte[]

    @Override
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        if (sourceOffset + numberToCopy <= leftLength) {
            left.copyToInternal(target, sourceOffset, targetOffset, numberToCopy);
        } else if (sourceOffset >= leftLength) {
            right.copyToInternal(target, sourceOffset - leftLength, targetOffset, numberToCopy);
        } else {
            int leftLength = this.leftLength - sourceOffset;
            left.copyToInternal(target, sourceOffset, targetOffset, leftLength);
            right.copyToInternal(target, 0, targetOffset + leftLength, numberToCopy - leftLength);
        }
    }

    @Override
    public void copyTo(ByteBuffer target) {
        left.copyTo(target);
        right.copyTo(target);
    }

    @Override
    public ByteBuffer asReadOnlyByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(toByteArray());
        return byteBuffer.asReadOnlyBuffer();
    }

    @Override
    public List<ByteBuffer> asReadOnlyByteBufferList() {
        // Walk through the list of LiteralBinary's that make up this
        // rope, and add each one as a read-only ByteBuffer.
        List<ByteBuffer> result = new ArrayList<>();
        PieceIterator pieces = new PieceIterator(this);
        while (pieces.hasNext()) {
            LiteralBinary byteString = pieces.next();
            result.add(byteString.asReadOnlyByteBuffer());
        }
        return result;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        left.writeTo(outputStream);
        right.writeTo(outputStream);
    }

    @Override
    public String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(toByteArray(), charsetName);
    }

    // =================================================================
    // UTF-8 decoding

    @Override
    public boolean isValidUtf8() {
        int leftPartial = left.partialIsValidUtf8(Utf8.COMPLETE, 0, leftLength);
        int state = right.partialIsValidUtf8(leftPartial, 0, right.size());
        return state == Utf8.COMPLETE;
    }

    @Override
    protected int partialIsValidUtf8(int state, int offset, int length) {
        int toIndex = offset + length;
        if (toIndex <= leftLength) {
            return left.partialIsValidUtf8(state, offset, length);
        } else if (offset >= leftLength) {
            return right.partialIsValidUtf8(state, offset - leftLength, length);
        } else {
            int leftLength = this.leftLength - offset;
            int leftPartial = left.partialIsValidUtf8(state, offset, leftLength);
            return right.partialIsValidUtf8(leftPartial, 0, length - leftLength);
        }
    }

    // =================================================================
    // equals() and hashCode()

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Binary)) {
            return false;
        }

        Binary otherBinary = (Binary) other;
        if (totalLength != otherBinary.size()) {
            return false;
        }
        if (totalLength == 0) {
            return true;
        }

        // You don't really want to be calling equals on long strings, but since
        // we cache the hashCode, we effectively cache inequality. We use the cached
        // hashCode if it's already computed. It's arguable we should compute the
        // hashCode here, and if we're going to be testing a bunch of byteStrings,
        // it might even make sense.
        if (hash != 0) {
            int cachedOtherHash = otherBinary.peekCachedHashCode();
            if (cachedOtherHash != 0 && hash != cachedOtherHash) {
                return false;
            }
        }

        return equalsFragments(otherBinary);
    }

    /**
     * Determines if this string is equal to another of the same length by iterating over the leaf nodes. On each step
     * of the iteration, the overlapping segments of the leaves are compared.
     *
     * @param other
     *            string of the same length as this one
     * @return true if the values of this string equals the value of the given one
     */
    private boolean equalsFragments(Binary other) {
        int thisOffset = 0;
        Iterator<LiteralBinary> thisIter = new PieceIterator(this);
        LiteralBinary thisString = thisIter.next();

        int thatOffset = 0;
        Iterator<LiteralBinary> thatIter = new PieceIterator(other);
        LiteralBinary thatString = thatIter.next();

        int pos = 0;
        while (true) {
            int thisRemaining = thisString.size() - thisOffset;
            int thatRemaining = thatString.size() - thatOffset;
            int bytesToCompare = Math.min(thisRemaining, thatRemaining);

            // At least one of the offsets will be zero
            boolean stillEqual = thisOffset == 0 ? thisString.equalsRange(thatString, thatOffset, bytesToCompare)
                    : thatString.equalsRange(thisString, thisOffset, bytesToCompare);
            if (!stillEqual) {
                return false;
            }

            pos += bytesToCompare;
            if (pos >= totalLength) {
                if (pos == totalLength) {
                    return true;
                }
                throw new IllegalStateException();
            }
            // We always get to the end of at least one of the pieces
            if (bytesToCompare == thisRemaining) { // If reached end of this
                thisOffset = 0;
                thisString = thisIter.next();
            } else {
                thisOffset += bytesToCompare;
            }
            if (bytesToCompare == thatRemaining) { // If reached end of that
                thatOffset = 0;
                thatString = thatIter.next();
            } else {
                thatOffset += bytesToCompare;
            }
        }
    }

    /**
     * Cached hash value. Intentionally accessed via a data race, which is safe because of the Java Memory Model's
     * "no out-of-thin-air values" guarantees for ints.
     */
    private int hash;

    @Override
    public int hashCode() {
        int h = hash;

        if (h == 0) {
            h = totalLength;
            h = partialHash(h, 0, totalLength);
            if (h == 0) {
                h = 1;
            }
            hash = h;
        }
        return h;
    }

    @Override
    protected int peekCachedHashCode() {
        return hash;
    }

    @Override
    protected int partialHash(int h, int offset, int length) {
        int toIndex = offset + length;
        if (toIndex <= leftLength) {
            return left.partialHash(h, offset, length);
        } else if (offset >= leftLength) {
            return right.partialHash(h, offset - leftLength, length);
        } else {
            int leftLength = this.leftLength - offset;
            int leftPartial = left.partialHash(h, offset, leftLength);
            return right.partialHash(leftPartial, 0, length - leftLength);
        }
    }

    // =================================================================
    // Input stream

    // @Override
    // public CodedInputStream newCodedInput() {
    // return CodedInputStream.newInstance(new RopeInputStream());
    // }

    @Override
    public InputStream newInput() {
        return new RopeInputStream();
    }

    // =================================================================
    // ByteIterator

    @Override
    public ByteIterator iterator() {
        return new RopeByteIterator();
    }

    /**
     * This class implements the balancing algorithm of BAP95. In the paper the authors use an array to keep track of
     * pieces, while here we use a stack. The tree is balanced by traversing subtrees in left to right order, and the
     * stack always contains the part of the string we've traversed so far.
     *
     * <p>
     * One surprising aspect of the algorithm is the result of balancing is not necessarily balanced, though it is
     * nearly balanced. For details, see BAP95.
     */
    static class Balancer {
        // Stack containing the part of the string, starting from the left, that
        // we've already traversed. The final string should be the equivalent of
        // concatenating the strings on the stack from bottom to top.
        private final Stack<Binary> prefixesStack = new Stack<>();

        Binary balance(Binary left, Binary right) {
            doBalance(left);
            doBalance(right);

            // Sweep stack to gather the result
            Binary partialString = prefixesStack.pop();
            while (!prefixesStack.isEmpty()) {
                Binary newLeft = prefixesStack.pop();
                partialString = new RopeBinary(newLeft, partialString);
            }
            // We should end up with a RopeBinary since at a minimum we will
            // create one from concatenating left and right
            return partialString;
        }

        private void doBalance(Binary root) {
            // BAP95: Insert balanced subtrees whole. This means the result might not
            // be balanced, leading to repeated rebalancings on concatenate. However,
            // these rebalancings are shallow due to ignoring balanced subtrees, and
            // relatively few calls to insert() result.
            if (root.isBalanced()) {
                insert(root);
            } else if (root instanceof RopeBinary) {
                RopeBinary rbs = (RopeBinary) root;
                doBalance(rbs.left);
                doBalance(rbs.right);
            } else {
                throw new IllegalArgumentException("Has a new type of Binary been created? Found " + root.getClass());
            }
        }

        /**
         * Push a string on the balance stack (BAP95). BAP95 uses an array and calls the elements in the array 'bins'.
         * We instead use a stack, so the 'bins' of lengths are represented by differences between the elements of
         * minLengthByDepth.
         *
         * <p>
         * If the length bin for our string, and all shorter length bins, are empty, we just push it on the stack.
         * Otherwise, we need to start concatenating, putting the given string in the "middle" and continuing until we
         * land in an empty length bin that matches the length of our concatenation.
         *
         * @param byteString
         *            string to place on the balance stack
         */
        private void insert(Binary byteString) {
            int depthBin = getDepthBinForLength(byteString.size());
            int binEnd = MIN_LENGTH_BY_DEPTH[depthBin + 1];

            // BAP95: Concatenate all trees occupying bins representing the length of
            // our new piece or of shorter pieces, to the extent that is possible.
            // The goal is to clear the bin which our piece belongs in, but that may
            // not be entirely possible if there aren't enough longer bins occupied.
            if (prefixesStack.isEmpty() || prefixesStack.peek().size() >= binEnd) {
                prefixesStack.push(byteString);
            } else {
                int binStart = MIN_LENGTH_BY_DEPTH[depthBin];

                // Concatenate the subtrees of shorter length
                Binary newTree = prefixesStack.pop();
                while (!prefixesStack.isEmpty() && prefixesStack.peek().size() < binStart) {
                    Binary left = prefixesStack.pop();
                    newTree = new RopeBinary(left, newTree);
                }

                // Concatenate the given string
                newTree = new RopeBinary(newTree, byteString);

                // Continue concatenating until we land in an empty bin
                while (!prefixesStack.isEmpty()) {
                    depthBin = getDepthBinForLength(newTree.size());
                    binEnd = MIN_LENGTH_BY_DEPTH[depthBin + 1];
                    if (prefixesStack.peek().size() < binEnd) {
                        Binary left = prefixesStack.pop();
                        newTree = new RopeBinary(left, newTree);
                    } else {
                        break;
                    }
                }
                prefixesStack.push(newTree);
            }
        }

        private int getDepthBinForLength(int length) {
            int depth = Arrays.binarySearch(MIN_LENGTH_BY_DEPTH, length);
            if (depth < 0) {
                // It wasn't an exact match, so convert to the index of the containing
                // fragment, which is one less even than the insertion point.
                int insertionPoint = -(depth + 1);
                depth = insertionPoint - 1;
            }

            return depth;
        }
    }

    /**
     * This class is a continuable tree traversal, which keeps the state information which would exist on the stack in a
     * recursive traversal instead on a stack of "Bread Crumbs". The maximum depth of the stack in this iterator is the
     * same as the depth of the tree being traversed.
     *
     * <p>
     * This iterator is used to implement {@link RopeBinary#equalsFragments(Binary)}.
     */
    static class PieceIterator implements Iterator<LiteralBinary> {

        private final Stack<RopeBinary> breadCrumbs = new Stack<>();

        private LiteralBinary next;

        PieceIterator(Binary root) {
            next = getLeafByLeft(root);
        }

        private LiteralBinary getLeafByLeft(Binary root) {
            Binary pos = root;
            while (pos instanceof RopeBinary) {
                RopeBinary rbs = (RopeBinary) pos;
                breadCrumbs.push(rbs);
                pos = rbs.left;
            }
            return (LiteralBinary) pos;
        }

        private LiteralBinary getNextNonEmptyLeaf() {
            while (true) {
                // Almost always, we go through this loop exactly once. However, if
                // we discover an empty string in the rope, we toss it and try again.
                if (breadCrumbs.isEmpty()) {
                    return null;
                } else {
                    LiteralBinary result = getLeafByLeft(breadCrumbs.pop().right);
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next item and advances one {@code LiteralBinary}.
         *
         * @return next non-empty LiteralBinary or {@code null}
         */
        public LiteralBinary next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            LiteralBinary result = next;
            next = getNextNonEmptyLeaf();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    class RopeByteIterator implements Binary.ByteIterator {

        private final PieceIterator pieces;

        private ByteIterator bytes;

        int bytesRemaining;

        RopeByteIterator() {
            pieces = new PieceIterator(RopeBinary.this);
            bytes = pieces.next().iterator();
            bytesRemaining = size();
        }

        public boolean hasNext() {
            return bytesRemaining > 0;
        }

        public Byte next() {
            return nextByte(); // Does not instantiate a Byte
        }

        public byte nextByte() {
            if (!bytes.hasNext()) {
                bytes = pieces.next().iterator();
            }
            --bytesRemaining;
            return bytes.nextByte();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * This class is the {@link RopeBinary} equivalent for {@link ByteArrayInputStream}.
     */
    private class RopeInputStream extends InputStream {
        // Iterates through the pieces of the rope
        private PieceIterator pieceIterator;

        // The current piece
        private LiteralBinary currentPiece;

        // The size of the current piece
        private int currentPieceSize;

        // The index of the next byte to read in the current piece
        private int currentPieceIndex;

        // The offset of the start of the current piece in the rope byte string
        private int currentPieceOffsetInRope;

        // Offset in the buffer at which user called mark();
        private int mark;

        public RopeInputStream() {
            initialize();
        }

        @Override
        public int read(byte[] b, int offset, int length) {
            if (b == null) {
                throw new NullPointerException();
            } else if (offset < 0 || length < 0 || length > b.length - offset) {
                throw new IndexOutOfBoundsException();
            }
            return readSkipInternal(b, offset, length);
        }

        @Override
        public long skip(long length) {
            if (length < 0) {
                throw new IndexOutOfBoundsException();
            } else if (length > Integer.MAX_VALUE) {
                length = Integer.MAX_VALUE;
            }
            return readSkipInternal(null, 0, (int) length);
        }

        /**
         * Internal implementation of read and skip. If b != null, then read the next {@code length} bytes into the
         * buffer {@code b} at offset {@code offset}. If b == null, then skip the next {@code length} bytes.
         * <p>
         * This method assumes that all error checking has already happened.
         * <p>
         * Returns the actual number of bytes read or skipped.
         */
        private int readSkipInternal(byte[] b, int offset, int length) {
            int bytesRemaining = length;
            while (bytesRemaining > 0) {
                advanceIfCurrentPieceFullyRead();
                if (currentPiece == null) {
                    if (bytesRemaining == length) {
                        // We didn't manage to read anything
                        return -1;
                    }
                    break;
                } else {
                    // Copy the bytes from this piece.
                    int currentPieceRemaining = currentPieceSize - currentPieceIndex;
                    int count = Math.min(currentPieceRemaining, bytesRemaining);
                    if (b != null) {
                        currentPiece.copyTo(b, currentPieceIndex, offset, count);
                        offset += count;
                    }
                    currentPieceIndex += count;
                    bytesRemaining -= count;
                }
            }
            // Return the number of bytes read.
            return length - bytesRemaining;
        }

        @Override
        public int read() throws IOException {
            advanceIfCurrentPieceFullyRead();
            if (currentPiece == null) {
                return -1;
            } else {
                return currentPiece.byteAt(currentPieceIndex++) & 0xFF;
            }
        }

        @Override
        public int available() throws IOException {
            int bytesRead = currentPieceOffsetInRope + currentPieceIndex;
            return RopeBinary.this.size() - bytesRead;
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public void mark(int readAheadLimit) {
            // Set the mark to our position in the byte string
            mark = currentPieceOffsetInRope + currentPieceIndex;
        }

        @Override
        public synchronized void reset() {
            // Just reinitialize and skip the specified number of bytes.
            initialize();
            readSkipInternal(null, 0, mark);
        }

        /** Common initialization code used by both the constructor and reset() */
        private void initialize() {
            pieceIterator = new PieceIterator(RopeBinary.this);
            currentPiece = pieceIterator.next();
            currentPieceSize = currentPiece.size();
            currentPieceIndex = 0;
            currentPieceOffsetInRope = 0;
        }

        /**
         * Skips to the next piece if we have read all the data in the current piece. Sets currentPiece to null if we
         * have reached the end of the input.
         */
        private void advanceIfCurrentPieceFullyRead() {
            if (currentPiece != null && currentPieceIndex == currentPieceSize) {
                // Generally, we can only go through this loop at most once, since
                // empty strings can't end up in a rope. But better to test.
                currentPieceOffsetInRope += currentPieceSize;
                currentPieceIndex = 0;
                if (pieceIterator.hasNext()) {
                    currentPiece = pieceIterator.next();
                    currentPieceSize = currentPiece.size();
                } else {
                    currentPiece = null;
                    currentPieceSize = 0;
                }
            }
        }
    }
}
