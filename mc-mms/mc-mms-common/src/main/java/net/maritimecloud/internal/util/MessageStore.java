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
package net.maritimecloud.internal.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.maritimecloud.util.Binary;

/**
 *
 * @author Kasper Nielsen
 */
public class MessageStore<T extends StoredMessage> {

    final static int HASH_LENGTH = 32;

    final long timeToLiveNanos = TimeUnit.NANOSECONDS.convert(1, TimeUnit.HOURS);

    /** All messages, keyed by hash. Is a skip list to allow for prefix search. */
    final ConcurrentSkipListMap<Binary, T> messages = new ConcurrentSkipListMap<>();

    /** All messages ordered by time, uses hash for breaking ties. */
    final ConcurrentSkipListMap<Key, T> messagesByTime = new ConcurrentSkipListMap<>();

    public void addMessage(T m) {
        messages.put(m.getMessageId(), m);
    }

    public T find(Binary key) {
        return messages.get(key);
    }

    public Set<T> findPrefixed(Binary prefix) {
        return Collections.emptySet();
    }

    public void forEach(Consumer<? super T> consumer) {
        messages.values().forEach(consumer);
    }

    public void forEachEldestFirst(Consumer<? super T> consumer) {
        messagesByTime.descendingMap().values().forEach(consumer);
    }

    public void clear() {
        // It would be easier to just clear the two maps.
        // But this would fail if we get a concurrent update
        pruneMessagesOldThan(Long.MAX_VALUE);
    }

    public void pruneMessages(Predicate<? super T> predicate) {
        for (Map.Entry<Key, T> e : messagesByTime.entrySet()) {
            if (predicate.test(e.getValue())) {
                messagesByTime.remove(e.getKey());
                messages.remove(e.getValue().getMessageId());
            }
        }
    }

    public void pruneMessagesOldThan(long timestamp) {
        pruneMessages(e -> e.getTimestamp() < timestamp);
    }


    static class Key implements Comparable<Key> {
        final StoredMessage msg;

        Key(StoredMessage msg) {
            this.msg = msg;
        }

        /** {@inheritDoc} */
        @Override
        public int compareTo(Key o) {
            if (msg.getTimestamp() == o.msg.getTimestamp()) {
                return msg.getMessageId().compareTo(o.msg.getMessageId());
            }
            return Long.compare(msg.getTimestamp(), o.msg.getTimestamp());
        }
    }
}
