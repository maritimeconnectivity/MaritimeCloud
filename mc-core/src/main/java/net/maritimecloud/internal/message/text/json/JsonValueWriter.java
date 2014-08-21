package net.maritimecloud.internal.message.text.json;

import static java.util.Objects.requireNonNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import net.maritimecloud.internal.message.TaggableMessageWriter;
import net.maritimecloud.internal.message.TaggableValueWriter;
import net.maritimecloud.internal.message.text.AbstractTextValueWriter;
import net.maritimecloud.message.Message;
import net.maritimecloud.message.MessageSerializer;
import net.maritimecloud.message.MessageWriter;
import net.maritimecloud.message.ValueSerializer;

public class JsonValueWriter extends AbstractTextValueWriter implements TaggableValueWriter, Closeable {

    /** The Unix line separator. */
    static final String LS = "\n";

    private int indent;

    private boolean isFirst = true;

    private final Writer pw;

    private final MessageWriter w = new TaggableMessageWriter(this);

    public JsonValueWriter(Writer w) {
        this.pw = requireNonNull(w);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        pw.close();
    }

    protected String escape(String string) {
        StringBuilder sb = new StringBuilder();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F' || ch >= '\u2000'
                && ch <= '\u20FF') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Adds the specified count of spaces to the specified string builder.
     *
     * @throws IOException
     */
    private void indent() throws IOException {
        for (int i = 0; i < indent; i++) {
            pw.append("  ");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void writeBoolean(Boolean value) throws IOException {
        pw.write(value.toString());
    }

    @Override
    protected void writeEscapedString(String value) throws IOException {
        pw.append("\"").append(value).append("\"");
    }

    public <T> void writeList(List<T> list, ValueSerializer<T> serializer) throws IOException {
        pw.write("[");
        indent++;
        boolean isFirst = true;
        for (T o : list) {
            if (!isFirst) {
                pw.write(",");
            }
            pw.write(LS);
            indent();
            serializer.write(o, this);
            // writeElement(o);
            isFirst = false;
        }
        pw.write(LS);
        indent--;
        indent();
        pw.append("]");
    }

    /** {@inheritDoc} */
    @Override
    public <K, V> void writeMap(Map<K, V> map, ValueSerializer<K> keySerializer, ValueSerializer<V> valueSerializer)
            throws IOException {
        pw.write("{}");
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Message> void writeMessage(T message, MessageSerializer<T> serializer) throws IOException {
        if (message != null) {
            pw.write("{");
            indent++;
            boolean isFirst = this.isFirst;
            this.isFirst = true;
            serializer.write(message, w);
            this.isFirst = isFirst;
            indent--;
            pw.write(LS);
            indent();
            pw.write("}");
        }
    }

    @Override
    protected void writeNumber(String value) throws IOException {
        pw.write(value);
    }

    public JsonValueWriter writeTag(int tag, String name) throws IOException {
        if (!isFirst) {
            pw.write(",");
        }
        isFirst = false;
        pw.write(LS);
        indent();
        pw.write("\"" + name + "\": ");
        return this;
    }
}
