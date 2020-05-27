package com.ruoyi.common.utils;

/*
 *  This file is part of org.opensky.libadsb.
 *
 *  org.opensky.libadsb is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  org.opensky.libadsb is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with org.opensky.libadsb.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Some useful functions when working with libadsb. Mostly we need these
 * functions since the library often works with arrays of bytes which are
 * not really readable for humans or basic operations are missing.
 * @author Matthias Schäfer (schaefer@opensky-network.org)
 */
public class ADSBTool {
    private static final char[] hexDigits =
            {'0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Converts a byte into a hex string (e.g. 164 -&gt; "a4")
     * @param b input byte
     * @return hex representation of input byte
     */
    public static String toHexString(byte b) {
        final char[] out = new char[2];
        out[0] = hexDigits[(0xF0 & b) >>> 4];
        out[1] = hexDigits[0x0F & b];
        return new String(out);
    }


    /**
     * Converts an array of bytes in a hex string; Taken from
     * org.apache.commons.codec.binary.Hex.
     * @param bytes array of bytes
     * @return concatenated hex representation of input byte array
     */
    public static String toHexString(byte[] bytes) {
        final int l = bytes.length;
        final char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = hexDigits[(0xF0 & bytes[i]) >>> 4];
            out[j++] = hexDigits[0x0F & bytes[i]];
        }
        return new String(out);
    }

    /**
     * Converts a hex string to an array of bytes.<br>
     * Source: https://stackoverflow.com/a/140861/3485023
     * @param str the hex string to convert
     * @return the byte array
     */
    public static byte[] hexStringToByteArray(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                    + Character.digit(str.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Compares two byte arrays element by element
     * @param array1 first array
     * @param array2 second array
     * @return array1 == array2
     */
    public static boolean areEqual(byte[] array1, byte[] array2) {
        if (array1.length != array2.length) return false;

        for (int i=0; i<array1.length; ++i)
            if (array1[i] != array2[i]) return false;

        return true;
    }

    /**
     * Compares two byte arrays element by element
     * @param array1 first array
     * @param array2 second array
     * @return array1 == array2
     */
    public static boolean areEqual(char[] array1, char[] array2) {
        if (array1.length != array2.length) return false;

        for (int i=0; i<array1.length; ++i)
            if (array1[i] != array2[i]) return false;

        return true;
    }

    /**
     * @param byte1 first byte
     * @param byte2 second byte
     * @return byte1 xor byte2 (bitwise)
     */
    public static byte xor(byte byte1, byte byte2) {
        return (byte)(0xff&(byte1^byte2));
    }

    /**
     * @param array1 first array
     * @param array2 second array
     * @return array1 xor array2 (bitwise)
     */
    public static byte[] xor(byte[] array1, byte[] array2) {
        assert(array1.length == array2.length);

        byte[] res = new byte[array1.length];
        for (int i=0; i<array1.length; ++i)
            res[i] = xor(array1[i], array2[i]);

        return res;
    }

    /**
     * Checks whether a byte array just contains elements equal to zero
     * @param  bytes input byte array
     * @return true if all bytes of the array are 0
     */
    public static boolean isZero(byte[] bytes) {
        int x = 0;
        for (int i = 0; i < bytes.length; i++) {
            x |= bytes[i];
        }
        return x == 0;
    }

    /**
     * Convert feet to meters, handling null inputs
     * @param ft value in feet
     * @return value in meters or null if input was null
     */
    public static Double feet2Meters(Integer ft) {
        if (ft == null) return null;
        return ft.doubleValue() * 0.3048;
    }

    /**
     * Convert feet to meters, handling null inputs
     * @param ft value in feet
     * @return value in meters or null if input was null
     */
    public static Double feet2Meters(Double ft) {
        if (ft == null) return null;
        return ft * 0.3048;
    }

    /**
     * Convert feet to meters, handling null inputs
     * @param m value in meters
     * @return value in feet or null if input was null
     */
    public static Double meters2Feet(Integer m) {
        if (m == null) return null;
        return m.doubleValue() / 0.3048;
    }

    /**
     * Convert feet to meters, handling null inputs
     * @param m value in meters
     * @return value in feet or null if input was null
     */
    public static Double meters2Feet(Double m) {
        if (m == null) return null;
        return m / 0.3048;
    }

    /**
     * Convert knots to meters per second, handling null inputs
     * @param kn value in knots
     * @return value in m/s or null if input was null
     */
    public static Double knots2MetersPerSecond(Integer kn) {
        if (kn == null) return null;
        return kn.doubleValue() * 0.514444;
    }

    /**
     * Convert knots to meters per second, handling null inputs
     * @param kn value in knots
     * @return value in m/s or null if input was null
     */
    public static Double knots2MetersPerSecond(Double kn) {
        if (kn == null) return null;
        return kn * 0.514444;
    }

    /**
     * Convert  feet per minute to meters per second, handling null inputs
     * @param ftmin value in ft/min
     * @return value in m/s or null if input was null
     */
    public static Double feetPerMinute2MetersPerSecond(Integer ftmin) {
        if (ftmin == null) return null;
        return ftmin.doubleValue() * 0.00508;
    }

    /**
     * Convert meters per second to knots, handling null inputs
     * @param mps value in meters per second
     * @return value in m/s or null if input was null
     */
    public static Double metersPerSecond2Knots(Integer mps) {
        if (mps == null) return null;
        return mps.doubleValue() / 0.514444;
    }

    /**
     * Convert meters per second to knots, handling null inputs
     * @param mps value in meters per second
     * @return value in knots or null if input was null
     */
    public static Double metersPerSecond2Knots(Double mps) {
        if (mps == null) return null;
        return mps / 0.514444;
    }

    /**
     * Convert meters per second to feet per minute, handling null inputs
     * @param mps value in meters per second
     * @return value in ft/min or null if input was null
     */
    public static Double metersPerSecond2FeetPerMinute(Integer mps) {
        if (mps == null) return null;
        return mps.doubleValue() / 0.00508;
    }
}

