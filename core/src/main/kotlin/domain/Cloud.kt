package domain

import kotlin.random.Random

class Cloud(
    private var x: Double,
    private val y: Int,
    private val width: Int,
    private val height: Int,
    private val blobs: List<Blob>
) {
    data class Blob(val offsetX: Int, val offsetY: Int, val radius: Int)

    fun tick(windStrength: Int) {
        x += windStrength * 0.3
    }

    fun x(): Int = x.toInt()
    fun y(): Int = y
    fun width(): Int = width
    fun height(): Int = height
    fun blobs(): List<Blob> = blobs

    fun isOffScreenRight(screenWidth: Int): Boolean = x > screenWidth + width
    fun isOffScreenLeft(): Boolean = x < -width

    companion object {
        fun random(screenWidth: Int): Cloud {
            val width = Random.nextInt(80, 201)
            val height = Random.nextInt(40, 81)
            val x = Random.nextDouble(-width.toDouble(), screenWidth.toDouble())
            val y = Random.nextInt(20, 180)

            val blobCount = Random.nextInt(3, 8)
            val blobs = (0 until blobCount).map {
                val offsetX = Random.nextInt(0, width)
                val offsetY = Random.nextInt(0, height)
                val radius = Random.nextInt(height / 3, height / 2 + 1)
                Blob(offsetX, offsetY, radius)
            }

            return Cloud(x, y, width, height, blobs)
        }

        fun spawnFromLeft(screenWidth: Int): Cloud {
            val cloud = random(screenWidth)
            return Cloud(-cloud.width().toDouble(), cloud.y(), cloud.width(), cloud.height(), cloud.blobs())
        }

        fun spawnFromRight(screenWidth: Int): Cloud {
            val cloud = random(screenWidth)
            return Cloud(screenWidth.toDouble(), cloud.y(), cloud.width(), cloud.height(), cloud.blobs())
        }
    }
}
