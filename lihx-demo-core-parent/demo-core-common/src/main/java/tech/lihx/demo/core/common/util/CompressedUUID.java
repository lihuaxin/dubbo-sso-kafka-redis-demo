package tech.lihx.demo.core.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

/**
 * <p>
 * Base58 is a way to encode Bitcoin addresses as numbers and letters. Note that
 * this is not the same base58 as used by Flickr, which you may see reference to
 * around the internet.
 * </p>
 *
 * <p>
 * You may instead wish to work with {@link VersionedChecksummedBytes}, which
 * adds support for testing the prefix and suffix bytes commonly found in
 * addresses.
 * </p>
 *
 * <p>
 * Satoshi says: why base-58 instead of standard base-64 encoding?
 * <p>
 *
 * <ul>
 * <li>Don't want 0OIl characters that look the same in some fonts and could be
 * used to create visually identical looking account numbers.</li>
 * <li>A string with non-alphanumeric characters is not as easily accepted as an
 * account number.</li>
 * <li>E-mail usually won't line-break if there's no punctuation to break at.</li>
 * <li>Doubleclicking selects the whole number as one word if it's all
 * alphanumeric.</li>
 * </ul>
 */
public class CompressedUUID {

	public static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

	private static final int[] INDEXES = new int[128];
	static {
		for ( int i = 0 ; i < INDEXES.length ; i++ ) {
			INDEXES[i] = -1;
		}
		for ( int i = 0 ; i < ALPHABET.length ; i++ ) {
			INDEXES[ALPHABET[i]] = i;
		}
	}


	/** Encodes the given bytes in base58. No checksum is appended. */
	public static String encode( byte[] value ) {
		if ( value.length == 0 ) { return ""; }
		byte[] input = copyOfRange(value, 0, value.length);
		// Count leading zeroes.
		int zeroCount = 0;
		while ( zeroCount < input.length && input[zeroCount] == 0 ) {
			++zeroCount;
		}
		// The actual encoding.
		byte[] temp = new byte[input.length * 2];
		int j = temp.length;

		int startAt = zeroCount;
		while ( startAt < input.length ) {
			byte mod = divmod58(input, startAt);
			if ( input[startAt] == 0 ) {
				++startAt;
			}
			temp[--j] = (byte) ALPHABET[mod];
		}

		// Strip extra '1' if there are some after decoding.
		while ( j < temp.length && temp[j] == ALPHABET[0] ) {
			++j;
		}
		// Add as many leading '1' as there were leading zeros.
		while ( --zeroCount >= 0 ) {
			temp[--j] = (byte) ALPHABET[0];
		}

		byte[] output = copyOfRange(temp, j, temp.length);
		try {
			return new String(output, "US-ASCII");
		} catch ( UnsupportedEncodingException e ) {
			throw new RuntimeException(e); // Cannot happen.
		}
	}


	public static byte[] decode( String input ) {
		if ( input.length() == 0 ) { return new byte[0]; }
		byte[] input58 = new byte[input.length()];
		// Transform the String to a base58 byte sequence
		for ( int i = 0 ; i < input.length() ; ++i ) {
			char c = input.charAt(i);

			int digit58 = -1;
			if ( c >= 0 && c < 128 ) {
				digit58 = INDEXES[c];
			}
			if ( digit58 < 0 ) { throw new RuntimeException("Illegal character " + c + " at " + i); }

			input58[i] = (byte) digit58;
		}
		// Count leading zeroes
		int zeroCount = 0;
		while ( zeroCount < input58.length && input58[zeroCount] == 0 ) {
			++zeroCount;
		}
		// The encoding
		byte[] temp = new byte[input.length()];
		int j = temp.length;

		int startAt = zeroCount;
		while ( startAt < input58.length ) {
			byte mod = divmod256(input58, startAt);
			if ( input58[startAt] == 0 ) {
				++startAt;
			}

			temp[--j] = mod;
		}
		// Do no add extra leading zeroes, move j to first non null byte.
		while ( j < temp.length && temp[j] == 0 ) {
			++j;
		}

		return copyOfRange(temp, j - zeroCount, temp.length);
	}


	public static BigInteger decodeToBigInteger( String input ) {
		return new BigInteger(1, decode(input));
	}


	/**
	 * Uses the checksum in the last 4 bytes of the decoded data to verify the
	 * rest are correct. The checksum is removed from the returned data.
	 *
	 * @throws AddressFormatException
	 *             if the input is not base 58 or the checksum does not
	 *             validate.
	 */
	public static byte[] decodeChecked( String input ) {
		byte tmp[] = decode(input);
		if ( tmp.length < 4 ) { throw new RuntimeException("Input to short"); }
		byte[] bytes = copyOfRange(tmp, 0, tmp.length - 4);
		byte[] checksum = copyOfRange(tmp, tmp.length - 4, tmp.length);

		tmp = doubleDigest(bytes, 0, bytes.length);
		byte[] hash = copyOfRange(tmp, 0, 4);
		if ( !Arrays.equals(checksum, hash) ) { throw new RuntimeException("Checksum does not validate"); }

		return bytes;
	}


	public static byte[] doubleDigest( byte[] input, int offset, int length ) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(input, offset, length);
			byte[] first = digest.digest();
			return digest.digest(first);
		} catch ( NoSuchAlgorithmException e ) {
			throw new RuntimeException(e); // Cannot happen.
		}
	}


	//
	// number -> number / 58, returns number % 58
	//
	private static byte divmod58( byte[] number, int startAt ) {
		int remainder = 0;
		for ( int i = startAt ; i < number.length ; i++ ) {
			int digit256 = number[i] & 0xFF;
			int temp = remainder * 256 + digit256;
			number[i] = (byte) (temp / 58);
			remainder = temp % 58;
		}
		return (byte) remainder;
	}


	//
	// number -> number / 256, returns number % 256
	//
	private static byte divmod256( byte[] number58, int startAt ) {
		int remainder = 0;
		for ( int i = startAt ; i < number58.length ; i++ ) {
			int digit58 = number58[i] & 0xFF;
			int temp = remainder * 58 + digit58;

			number58[i] = (byte) (temp / 256);

			remainder = temp % 256;
		}

		return (byte) remainder;
	}


	private static byte[] copyOfRange( byte[] source, int from, int to ) {
		byte[] range = new byte[to - from];
		System.arraycopy(source, from, range, 0, range.length);

		return range;
	}


	public static String compressedUUID( UUID uuid ) {
		byte[] byUuid = new byte[16];
		long least = uuid.getLeastSignificantBits();
		long most = uuid.getMostSignificantBits();
		long2bytes(most, byUuid, 0);
		long2bytes(least, byUuid, 8);
		String compressUUID = encode(byUuid);
		return compressUUID;
	}


	public static String compressedUUID() {
		UUID uuid = UUID.randomUUID();
		return compressedUUID(uuid);
	}


	protected static void long2bytes( long value, byte[] bytes, int offset ) {
		int offsetLocal = offset;
		for ( int i = 7 ; i > -1 ; i-- ) {
			bytes[offsetLocal++] = (byte) ((value >> 8 * i) & 0xFF);
		}
	}


	public static void main( String[] args ) {
		System.out.println(compressedUUID(UUID.randomUUID()));

		// long start = System.currentTimeMillis();
		// for (int i = 0; i < 100000; i++) {
		// compressedUUID(UUID.randomUUID());
		// }
		// long end = System.currentTimeMillis();
		// System.out.println(end - start);
	}
}
