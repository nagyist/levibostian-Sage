package earth.levi.sage.util

import earth.levi.sage.type.Photo

interface SamplePhotosUtil {
    fun getSamplePhotos(randomOrder: Boolean): List<Photo>
}

class SamplePhotosUtilImp: SamplePhotosUtil {
    override fun getSamplePhotos(randomOrder: Boolean): List<Photo> {
        val samplePhotos = listOf(
            "https://unsplash.com/photos/ohjrzAe3sic/download?ixid=MnwxMjA3fDB8MXxhbGx8OTd8fHx8fHwyfHwxNjU5OTg4OTE1&force=true&w=640",
            "https://unsplash.com/photos/4fPDsBL0l0M/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzExODY3&force=true&w=640",
            "https://unsplash.com/photos/q6HEw3JNw5M/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEyNzM1&force=true&w=640",
            "https://unsplash.com/photos/cNm6BPHZivc/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEzMDI5&force=true&w=640",
            "https://unsplash.com/photos/dFTWuA7ow0k/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMjk4NjI4&force=true&w=640",
            "https://unsplash.com/photos/NiYn_jB1AHY/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzExOTY2&force=true&w=640",
            "https://unsplash.com/photos/O2aYBEeb7hY/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEyMTAz&force=true&w=640",
            "https://unsplash.com/photos/SM8vfI1emg0/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEzMTQx&force=true&w=640",
            "https://unsplash.com/photos/IJc5PwW7HOE/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzA5NTQ3&force=true&w=640",
            "https://unsplash.com/photos/FqqaJI9OxMI/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzExNjg1&force=true&w=640",
            "https://unsplash.com/photos/RwS9lbEprx4/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEzODA0&force=true&w=640",
            "https://unsplash.com/photos/RclwpCDvmhk/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzExOTky&force=true&w=640",
            "https://unsplash.com/photos/Ud1THd4RBQQ/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEwOTcx&force=true&w=640",
            "https://unsplash.com/photos/KEyGBkJtrQo/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMjk5NjQy&force=true&w=640",
            "https://unsplash.com/photos/f626lgQvTQo/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzEzOTU3&force=true&w=640",
            "https://unsplash.com/photos/j-VNJn1rrQk/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MDI1&force=true&w=640",
            "https://unsplash.com/photos/g4sE94Sw02E/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MDc3&force=true&w=640",
            "https://unsplash.com/photos/wBcIdP_RKpM/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MDc4&force=true&w=640",
            "https://unsplash.com/photos/G5GBONCnEqE/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MDg5&force=true&w=640",
            "https://unsplash.com/photos/LYwOzQce1JM/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MDkz&force=true&w=640",
            "https://unsplash.com/photos/Xp6ThOPFOzo/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjYwMzE0MTEw&force=true&w=640"
        ).mapIndexed { _, url -> Photo.remote(url) }.toMutableList()

        if (randomOrder) samplePhotos.shuffle()

        return samplePhotos
    }
}