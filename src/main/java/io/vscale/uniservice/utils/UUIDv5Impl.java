package io.vscale.uniservice.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * 22.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 * source : https://stackoverflow.com/questions/40230276/how-to-make-a-type-5-uuid-in-java
 *
 */
public class UUIDv5Impl {

    private static final String ENCODING = "UTF-8";
    public static final UUID NAMESPACE_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_OID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_X500 = UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8");

    public static UUID nameUUIDFromNamespaceAndString(UUID namespace, String name) {

        UUID result;
        String stringToEncode = Objects.requireNonNull(name, "String for encode is null");

        try {
            result =  nameUUIDFromNamespaceAndBytes(namespace, stringToEncode.getBytes(ENCODING));
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError("Encoding not supported");
        }

        return result;
    }

    private static UUID fromBytes(byte[] data) {

        long msb = 0;
        long lsb = 0;

        int limit1 = 8;
        int limit2 = 16;

        assert data.length >= limit2;

        for (int i = 0; i < limit1; i++) {
            msb = (msb << limit1) | (data[i] & 0xff);
        }

        for (int i = limit1; i < limit2; i++) {
            lsb = (lsb << limit1) | (data[i] & 0xff);
        }

        return new UUID(msb, lsb);
    }

    private static byte[] toBytes(UUID uuid) {

        int limit2 = 16;
        byte[] out = new byte[limit2];
        int limit1 = 8;

        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        IntStream.range(0, limit1).forEach(i -> out[i] = (byte) ((msb >> ((7 - i) * limit1)) & 0xff));

        IntStream.range(limit1, limit2).forEach(i -> out[i] = (byte) ((lsb >> ((15 - i) * limit1)) & 0xff));

        return out;
    }

    private static UUID nameUUIDFromNamespaceAndBytes(UUID namespace, byte[] name){

        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalError("SHA-1 not supported");
        }

        md.update(toBytes(Objects.requireNonNull(namespace, "Namespace for encode is null")));
        md.update(Objects.requireNonNull(name, "String for encode is null"));

        byte[] sha1Bytes = md.digest();
        sha1Bytes[6] &= 0x0f;
        sha1Bytes[6] |= 0x50;
        sha1Bytes[8] &= 0x3f;
        sha1Bytes[8] |= 0x80;

        return fromBytes(sha1Bytes);

    }

}
