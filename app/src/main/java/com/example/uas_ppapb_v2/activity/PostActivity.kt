package com.example.uas_ppapb_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.database.model.FavoritePost
import com.example.uas_ppapb_v2.databinding.ActivityPostBinding
import com.example.uas_ppapb_v2.view.fragment.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class PostActivity : AppCompatActivity() {

    private val binding: ActivityPostBinding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }

    private val roomDBManager: CustomApp.RoomDBManager by lazy {
        (application as CustomApp).getRoomDBManager()
    }

    private val fireStoreManager: CustomApp.FireStoreManager by lazy {
        (application as CustomApp).returnFireStoreManager()
    }

    private lateinit var calendar: Calendar
    private var favorited: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        calendar = Calendar.getInstance()


        val postID = intent.getStringExtra("postID")
        val post = fireStoreManager.getPost(postID!!)
        val dayInWeek = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val month = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December")
        val currentDate = setDate()
        val newDate = getDate(post!!.lengthOfStay)
        checkIfBeenFavorited()
        roomDBManager.insertAccountRoom()

        with(binding) {
            if(post != null) {
                itemDestination.text = post.destination
                startAndEnd.text = post.startingStation + " to " + post.endStation
                overviewDescription.text = post.overviewDescription
                price.text = "Rp. " + post.price.toString()
                inDateDay.text = currentDate[0].toString()
                inDateDayInWeek.text = dayInWeek[currentDate[1] - 1]
                inDateMonthYear.text = month[currentDate[2] - 1]  + " " + currentDate[3].toString()
                imageView.load(post.imageURI) {
                    placeholder(imageView.drawable)
                    error(imageView.drawable)
                }
                outDateDay.text = newDate[0].toString()
                outDateDayInWeek.text = dayInWeek[newDate[1] - 1]
                outDateMonthYear.text = month[newDate[2] - 1]  + " " + newDate[3].toString()
            }

            returnButton.setOnClickListener {
                finish()
            }

            favoriteButton.setOnClickListener {
                if(!favorited) {
                    roomDBManager.insertFavorite(
                        FavoritePost(
                            id = post!!.id,
                            destination = post.destination,
                            startingStation = post.startingStation,
                            endStation = post.endStation,
                            shortDescription = post.shortDescription,
                            overviewDescription = post.overviewDescription,
                            price = post.price,
                            lengthOfStay = post.lengthOfStay,
                            imageURI = post.imageURI,
                            userUID = roomDBManager.getUID()
                        )
                    )
                    favoriteButton.setImageDrawable(getDrawable(R.drawable.baseline_favorite_36))
                    favorited = true
                    Snackbar.make(root, "Added to favorites", Snackbar.LENGTH_SHORT).show()
                } else {
                    roomDBManager.deleteFavorite(
                        FavoritePost(
                            id = post!!.id,
                            destination = post.destination,
                            startingStation = post.startingStation,
                            endStation = post.endStation,
                            shortDescription = post.shortDescription,
                            overviewDescription = post.overviewDescription,
                            price = post.price,
                            lengthOfStay = post.lengthOfStay,
                            imageURI = post.imageURI,
                            userUID = roomDBManager.getUID()
                        )
                    )
                    favorited = false
                    favoriteButton.setImageDrawable(getDrawable(R.drawable.baseline_favorite_border_36))
                    Snackbar.make(root, "Removed from favorites", Snackbar.LENGTH_SHORT).show()
                }
            }
            addToPlanButton.setOnClickListener {
                val bottomSheetFragment = BottomSheetDialog()
                bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
            }
        }

    }

    private fun checkIfBeenFavorited() {
        roomDBManager.getFavorites().observe(this)
        {
            it.forEach { favoritePost ->
                if(favoritePost.id == intent.getStringExtra("postID")) {
                    favorited = true
                    changeDrawable()
                }
            }
        }
    }

    private fun changeDrawable() {
        if(favorited) {
            binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.baseline_favorite_36))
        } else {
            binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.baseline_favorite_border_36))
        }
    }

    private fun getDate(lengthOfStay: Int): List<Int> {
        calendar.add(Calendar.DAY_OF_MONTH,lengthOfStay)
        val newDay = calendar.get(Calendar.DAY_OF_MONTH)
        val newDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val newMonth = calendar.get(Calendar.MONTH) + 1
        val newYear = calendar.get(Calendar.YEAR)
        calendar = Calendar.getInstance()
        return listOf(newDay, newDayOfTheWeek, newMonth, newYear)
    }
    private fun setDate(): List<Int> {
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        return listOf(currentDay, currentDayOfTheWeek, currentMonth, currentYear)
    }
}