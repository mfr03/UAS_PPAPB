package com.example.uas_ppapb_v2.database.model

data class Post(
    var id: String = "",
    var destination: String = "",
    var startingStation: String = "Mamba Mentality",
    var endStation: String = "Samba Mentality",
    var price: Int = 100,
    var lengthOfStay: Int = 3,
    var overviewDescription: String = "lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
    var shortDescription: String = "lorem ipsum dolor sit amet .. ",
    var tag: String = "",
    var imageURI: String = "",
)