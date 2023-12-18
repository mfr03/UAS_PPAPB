package com.example.uas_ppapb_v2.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uas_ppapb_v2.database.model.Account
import com.example.uas_ppapb_v2.database.model.AccountRoom
import com.example.uas_ppapb_v2.database.model.FavoritePost
import com.example.uas_ppapb_v2.database.model.PlannedPost
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.database.room.UserDatabaseRoom
import com.example.uas_ppapb_v2.sharedpreferences.SessionManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import java.util.concurrent.Executors

class CustomApp: Application() {

    // firebase
    private lateinit var firestore : FirebaseFirestore
    private val accountCollectionReference by lazy {
        firestore.collection("accounts")
    }

    private val postCollectionReference by lazy {
        firestore.collection("posts")
    }

    private val authenticationManager by lazy {
        AuthenticationManager()
    }

    private val database by lazy {
        UserDatabaseRoom.getDatabase(this)
    }

    private val fireStoreManager by lazy {
        FireStoreManager()
    }

    fun getAuthManager(): AuthenticationManager {
        return authenticationManager
    }

    fun returnFireStoreManager(): FireStoreManager {
        return fireStoreManager
    }

    fun getRoomDBManager(): RoomDBManager {
        return RoomDBManager()
    }

    private fun getAccountUID(): String {
        return sessionManager.getUID()
    }

    // shared preferences
    private val sessionManager by lazy {
        SessionManager.getInstance(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "extras",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Planned post notifications"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class AuthenticationManager() {

        private val adminsLiveData: MutableLiveData<List<Triple<String, String, String>>> by lazy {
            MutableLiveData<List<Triple<String, String, String>>>()
        }
        fun setLoggedIn(isLoggedIn: Boolean, isAdmin: Boolean) {
            sessionManager.setLoggedIn(isLoggedIn, isAdmin)
        }

        fun isLoggedIn(): Boolean {
            return sessionManager.isLoggedIn()
        }

        fun isAdmin(): Boolean {
            return sessionManager.isAdmin()
        }

        fun getAdmins(onSuccess: (MutableLiveData<List<Triple<String, String, String>>>) -> Unit) {
            val temp: MutableList<Triple<String, String, String>> = mutableListOf()
            if(sessionManager.isAdmin()) {
                accountCollectionReference.whereEqualTo("type", "admin").get()
                    .addOnSuccessListener {
                            query ->
                        for(document in query) {
                            val data = document.data
                            Log.d("ADMIN", data.get("username").toString())
                            temp.add(Triple(data.get("uid").toString(), data.get("username").toString(), data.get("email").toString()))
                        }
                        adminsLiveData.postValue(temp)
                        onSuccess(adminsLiveData)
                    }
            }

        }

        fun returnAccount(): Account {
            return sessionManager.getAccount()
        }

        fun getAccount() {
            getAccountFirebase()
        }

        fun loginAccount(email: String, password: String,
                         onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            loginAccountFirebase(email, password, onSuccess, onFailed)
        }

        fun registerAccount(email: String, password: String, username: String, dateOfBirth: String, nim: String, bypass: Boolean = false,
                            onSuccess: () -> Unit, onFailed: (Exception) -> Unit) {
            if(!bypass) {
                makeAccountFirebase(email, password, username, dateOfBirth, nim, onSuccess = onSuccess, onFailed = onFailed)
            } else if (bypass && sessionManager.isAdmin()) {
                makeAccountFirebase(email, password, username, dateOfBirth, nim, bypass = true, onSuccess, onFailed)
            } else {
                onFailed(java.lang.Exception("You are not an admin"))
            }

        }

        fun logout() {
            FirebaseAuth.getInstance().signOut()
            sessionManager.clear()
        }

        private fun getAccountFirebase() {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            accountCollectionReference.whereEqualTo("uid", user?.uid.toString()).limit(1).get()
                .addOnSuccessListener {
                        query ->
                    Log.d("LoginStack", "getAccountFirebase: ${user?.uid}")
                    val data = query.documents[0].data
                    sessionManager.setAccount(
                        data?.get("uid").toString(),
                        data?.get("username").toString(),
                        data?.get("email").toString(),
                        data?.get("dateOfBirth").toString(),
                        data?.get("nim").toString()
                    )
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }

        private fun loginAccountFirebase(email: String, password: String,
                                         onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { account ->
                    // getting account data from firestore with specific UID
                    val query: Query = accountCollectionReference.whereEqualTo("uid", account.user?.uid.toString()).limit(1)
                    query.get()
                        .addOnSuccessListener {
                                document ->
                            if(document.isEmpty) {
                                onFailed(Exception("Account not found"))
                            } else {
                                val data = document.documents[0].data
                                if(data?.get("type") == "user") {
                                    onSuccess(false)
                                } else if(data?.get("type") == "admin") {
                                    onSuccess(true)
                                } else {
                                    onFailed(Exception("Account type not found"))
                                }
                            }
                        }
                        .addOnFailureListener {
                            onFailed(it)
                        }
                }
                .addOnFailureListener {
                    onFailed(it)
                }
        }


        private fun makeAccountFirebase(email: String, password: String, username: String, dateOfBirth: String, nim: String, bypass: Boolean = false,
        onSuccess: () -> Unit, onFailed: (Exception) -> Unit){
            // TODO("NEED TO IMPLEMENT A CHECK ON ADDING TO FIREBASE")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { user ->
                    val account: Account
                    if(bypass) {
                        account = Account(
                            uid = user.user?.uid.toString(),
                            username = username,
                            email = email,
                            nim = nim,
                            type = "admin",
                            dateOfBirth = dateOfBirth,
                        )
                    } else {
                        account = Account(
                            uid = user.user?.uid.toString(),
                            username = username,
                            email = email,
                            nim = nim,
                            dateOfBirth = dateOfBirth,
                        )
                    }

                    accountCollectionReference.add(account)
                        .addOnSuccessListener { documentReference ->
                            documentReference.set(account)
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener {
                                    onFailed(it)
                                }
                        }
                        .addOnFailureListener {
                            onFailed(it)
                        }
                }
                .addOnFailureListener {
                    onFailed(it)
                }
        }
    }

    inner class FireStoreManager() {

        private val postLiveData: MutableLiveData<List<Post>> by lazy {
            MutableLiveData<List<Post>>()
        }

        fun addPost(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            addPostFirebase(post, onSuccess, onFailed)
        }

        fun updatePost(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            updatePostFirebase(post, onSuccess, onFailed)
        }

        fun deletePost(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            deletePostFirebase(post, onSuccess, onFailed)
        }

        fun getAllPost() {
            observePostChanges()
        }

        fun getPost(postId: String): Post? {
            getAllPost()
            postLiveData.value?.forEach {
                if(it.id == postId) {
                    return it
                }
            }
            return null
        }

        fun returnPostLiveData(): MutableLiveData<List<Post>> {
            return postLiveData
        }

        private fun observePostChanges() {
            postCollectionReference.addSnapshotListener { snapshot, error ->
                if(error != null) {
                    Log.d("CustomApp", "observePostChanges: ${error.message}")
                }
                val posts = snapshot?.toObjects(Post::class.java)
                if(posts != null) {
                    postLiveData.postValue(posts)
                }
            }
        }

        private fun addPostFirebase(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            postCollectionReference.add(post)
                .addOnSuccessListener { documentReference ->
                    post.id = documentReference.id
                    documentReference.set(post)
                        .addOnSuccessListener {
                            onSuccess(true)
                        }
                        .addOnFailureListener {
                            onFailed(it)
                        }
                }
                .addOnFailureListener {
                    onFailed(it)
                }
        }


        private fun updatePostFirebase(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {
            postCollectionReference.document(post.id).set(post)
                .addOnSuccessListener {
                    onSuccess(true)
                }
                .addOnFailureListener {
                    onFailed(it)
                }
        }

        private fun deletePostFirebase(post: Post, onSuccess: (Boolean) -> Unit, onFailed: (Exception) -> Unit) {

            if(post.id.isBlank()){
                onFailed(Exception("Post id is blank"))
                return
            }

            postCollectionReference.document(post.id).delete()
                .addOnSuccessListener {
                    onSuccess(true)
                }
                .addOnFailureListener {
                    onFailed(it)
                }
        }
    }

    inner class RoomDBManager() {
        private val favoriteDao = database!!.favoritePostDAO()!!
        private val accountRoomDao = database!!.accountRoomDAO()!!
        private val plannedPostDao = database!!.plannedPostDAO()!!
        private val executorService = Executors.newSingleThreadExecutor()


        fun insertAccountRoom() {
            executorService.execute {
                accountRoomDao.insertAccountRoom(AccountRoom(getUID()))
            }
        }

        fun getUID(): String {
            return getAccountUID()
        }

        fun insertPlannedPost(plannedPost: PlannedPost) {
            executorService.execute {
                plannedPostDao.insertPlannedPost(plannedPost)
            }
        }

        fun deletePlannedPost(plannedPost: PlannedPost) {
            executorService.execute {
                plannedPostDao.deletePlannedPost(plannedPost)
            }
        }

        fun getPlannedPosts(): LiveData<List<PlannedPost>> {
            return plannedPostDao.getAllPlannedPosts(getUID())
        }

        fun getFavorites(): LiveData<List<FavoritePost>> {
            return favoriteDao.getAllFavorites(getUID())
        }

        fun insertFavorite(favoritePost: FavoritePost) {
            executorService.execute {
                favoriteDao.insertFavorite(favoritePost)
            }
        }

        fun deleteFavorite(favoritePost: FavoritePost) {
            executorService.execute {
                favoriteDao.deleteFavorite(favoritePost)
            }
        }

    }
}