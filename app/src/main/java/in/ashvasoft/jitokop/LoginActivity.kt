package `in`.ashvasoft.jitokop

import `in`.adityaitsolutions.gst.Helpers.SharedPrefManager
import `in`.adityaitsolutions.gst.Objects.EndPoints
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    var sm: SharedPrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "JITO-Kolhapur"
        sm = SharedPrefManager(this)
        if(sm!!.getInstance(this@LoginActivity).isLoggedIn()){
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        btnLogin.setOnClickListener{
            if(etMobile.equals("")){
                etMobile.setError("Please enter mobile number!")
            }
            else{
                if(etMobile.text.toString().trim().length != 10){
                    etMobile.setError("Please check mobile number!")
                }
                else{
                    generateOTP(etMobile.text.toString())
                }
            }
        }
    }

    fun generateOTP(mobile:String){
//        val mProgressDialog = indeterminateProgressDialog("Please Wait", "OTP is Generating")
//        mProgressDialog.show()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_GENERATE_OTP,
            Response.Listener<String> { response ->
                try {
                    //mProgressDialog.dismiss()
                    val obj = JSONObject(response)
                    if(obj.getBoolean("error")){
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                    else{
                        //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                        val intent = Intent(this,OTPActivity::class.java)
                        intent.putExtra("mobile",mobile)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: JSONException) {
                    //mProgressDialog.dismiss()
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                    //mProgressDialog.dismiss()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("mobile", mobile)
                return params
            }
        }
        queue.add(stringRequest)
    }
}
