package com.kenzie.app;

/**
 * Represents a custom exception for use with HTTP response codes.
 * In the CustomHttpClient class I want to throw this exception
 * for any response code other than '200'.
 *
 * @author Ethan Tauriainen
 */
public class ResponseCodeException extends Exception {
    public ResponseCodeException(String msg) {
        super(msg);
    }
}
