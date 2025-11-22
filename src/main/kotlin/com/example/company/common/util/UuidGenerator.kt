package com.example.company.common.util

import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

/**
 * UUID v7 generator following RFC 9562.
 *
 * UUID v7 features:
 * - Time-ordered for better database performance
 * - 48-bit timestamp (millisecond precision)
 * - 74 bits of randomness
 * - Sortable by creation time
 * - Compatible with standard UUID type
 *
 * Format: xxxxxxxx-xxxx-7xxx-xxxx-xxxxxxxxxxxx
 * - First 48 bits: Unix timestamp in milliseconds
 * - Next 4 bits: Version (7)
 * - Next 12-14 bits: Random
 * - Next 2 bits: Variant (10)
 * - Last 62 bits: Random
 */
object UuidGenerator {
    private val random = SecureRandom()

    /**
     * Generate a new UUID v7.
     *
     * @return A time-ordered UUID v7
     */
    fun generate(): UUID {
        return generateV7(Instant.now())
    }

    /**
     * Generate a UUID v7 for a specific timestamp.
     *
     * @param timestamp The timestamp to use
     * @return A time-ordered UUID v7
     */
    fun generateV7(timestamp: Instant): UUID {
        val timestampMs = timestamp.toEpochMilli()

        // 48-bit timestamp (6 bytes)
        val timestampBytes = ByteArray(6)
        timestampBytes[0] = (timestampMs ushr 40).toByte()
        timestampBytes[1] = (timestampMs ushr 32).toByte()
        timestampBytes[2] = (timestampMs ushr 24).toByte()
        timestampBytes[3] = (timestampMs ushr 16).toByte()
        timestampBytes[4] = (timestampMs ushr 8).toByte()
        timestampBytes[5] = timestampMs.toByte()

        // 80 bits of randomness (10 bytes)
        val randomBytes = ByteArray(10)
        random.nextBytes(randomBytes)

        // Combine timestamp and random bytes
        val uuidBytes = ByteArray(16)
        System.arraycopy(timestampBytes, 0, uuidBytes, 0, 6)
        System.arraycopy(randomBytes, 0, uuidBytes, 6, 10)

        // Set version to 7 (bits 48-51)
        uuidBytes[6] = ((uuidBytes[6].toInt() and 0x0F) or 0x70).toByte()

        // Set variant to 10 (bits 64-65)
        uuidBytes[8] = ((uuidBytes[8].toInt() and 0x3F) or 0x80).toByte()

        // Convert to UUID
        var mostSigBits = 0L
        for (i in 0..7) {
            mostSigBits = (mostSigBits shl 8) or (uuidBytes[i].toLong() and 0xFF)
        }

        var leastSigBits = 0L
        for (i in 8..15) {
            leastSigBits = (leastSigBits shl 8) or (uuidBytes[i].toLong() and 0xFF)
        }

        return UUID(mostSigBits, leastSigBits)
    }

    /**
     * Extract the timestamp from a UUID v7.
     *
     * @param uuid The UUID v7 to extract the timestamp from
     * @return The timestamp in milliseconds since epoch
     */
    fun extractTimestamp(uuid: UUID): Long {
        val mostSigBits = uuid.mostSignificantBits
        return mostSigBits ushr 16
    }

    /**
     * Extract the Instant from a UUID v7.
     *
     * @param uuid The UUID v7 to extract the instant from
     * @return The Instant
     */
    fun extractInstant(uuid: UUID): Instant {
        return Instant.ofEpochMilli(extractTimestamp(uuid))
    }
}
