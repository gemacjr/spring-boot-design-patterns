package com.example.designpatterns.structural.decorator;

/**
 * <b>Concrete Decorator</b>: transforms the payload before passing it on.
 *
 * <p><b>ROT13 is not encryption.</b> It is used here purely because it is
 * self-inverse and readable in test output. Never use this for anything real.
 */
public class EncryptingSender extends MessageSenderDecorator {

    public EncryptingSender(MessageSender delegate) {
        super(delegate);
    }

    @Override
    public String send(String message) {
        return delegate.send(encrypt(message));
    }

    /** Self-inverse: applying it twice returns the original. */
    public static String encrypt(String input) {
        StringBuilder out = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                out.append((char) ('a' + (c - 'a' + 13) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                out.append((char) ('A' + (c - 'A' + 13) % 26));
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
