package com.jonasdevrient.citypinboard.models

/**
 * Static converter class to get the according image
 */
object StadFoto {
    /**
     * Gets an image url
     * @return the url of the image based on [stad]
     */
    fun image(stad: String): String {
        var answer = "http://doesnotexists.com"
        when {
            stad.toLowerCase() == "gent" -> answer = "https://www.dewarande.be/sites/default/files/gent2.jpg"
            stad.toLowerCase() == "oudenaarde" -> answer = "https://www.oudenaarde.be/sites/default/files/public/toerisme/Afbeeldingen/pamele-1-2729.jpg"
            stad.toLowerCase() == "zingem" -> answer = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Zingem_-_Belgium_-_Town_hall.jpg/1200px-Zingem_-_Belgium_-_Town_hall.jpg"
            stad.toLowerCase() == "namen" -> answer = "https://thumbs.werkaandemuur.nl/f4e3b1567e2984f5df08b88152652623_500x500_fit.jpg"
            stad.toLowerCase() == "brugge" -> answer = "https://www.brugge.be/files/uploads/imagecache/nexHVDBNewsImageDetail/images/news/stadhuis.jpg"
        }
        return answer
    }
}