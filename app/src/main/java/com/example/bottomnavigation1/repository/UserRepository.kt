import androidx.lifecycle.MutableLiveData
import com.example.bottomnavigation1.api.UserApi
import com.example.bottomnavigation1.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {
    private val baseUrl = "https://your-api-base-url.com/" // 替换为实际的API基地址

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userApi: UserApi = retrofit.create(UserApi::class.java)

    private val _usersLiveData = MutableLiveData<List<User>>()
    val usersLiveData: MutableLiveData<List<User>> = _usersLiveData

    private val _userByIdLiveData = MutableLiveData<User?>()
    val userByIdLiveData: MutableLiveData<User?> = _userByIdLiveData

    // 获取所有用户数据
    fun getAllUsers(){
        val call = userApi.getUsers()
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    _usersLiveData.value = response.body()
                } else {
                    // 可在此处添加处理获取用户数据失败的逻辑
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // 可在此处添加处理网络请求失败的逻辑
            }
        })
    }

    // 根据ID获取单个用户数据
    fun getUserById(id: Int) {
        val call = userApi.getUserById(id)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _userByIdLiveData.value = response.body()
                } else {
                    // 可在此处添加处理获取单个用户数据失败的逻辑
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // 可在此处添加处理网络请求失败的逻辑
            }
        }
        )
    }

    fun insertUser(user: User) {
        val call = userApi.insertUser(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    getAllUsers()
                } else {
                    // 可在此处添加处理插入用户数据失败的逻辑
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // 可在此处添加处理网络请求失败的逻辑
            }
        }
        )
    }

    // 更新用户数据（示例：根据ID更新用户邮箱）
    fun updateUser(id: Int, user: User) {
        val call = userApi.updateUser(id, user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    getAllUsers()
                } else {
                    // 可在此处添加处理更新用户数据失败的逻辑
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // 可在此处添加处理网络请求失败的逻辑
            }
        }
        )
    }

    // 删除用户数据（示例：根据ID删除用户）
    fun deleteUser(id: Int) {
        val call = userApi.deleteUser(id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getAllUsers()
                } else {
                    // 可在此处添加处理删除用户数据失败的逻辑
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 可在此处添加处理网络请求失败的逻辑
            }
        }
        )
    }

}





// 插入新用户数据



