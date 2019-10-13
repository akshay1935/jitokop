package `in`.ashvasoft.jitokop

import `in`.adityaitsolutions.gst.Helpers.SharedPrefManager
import `in`.adityaitsolutions.gst.Objects.EndPoints
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_otp.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONException
import org.json.JSONObject

class OTPActivity : AppCompatActivity() {
    var sm: SharedPrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar!!.title = "JITO-Kolhapur"
        sm = SharedPrefManager(this)
        var mobile = intent.getStringExtra("mobile")
        btnSubmit.setOnClickListener{
            if(etOTP.text.toString().trim().equals("")){
                etOTP.setError("Please enter OTP!")
            }
            else {
                if(etOTP.text.toString().trim().length!=4){
                    etOTP.setError("Please enter 4 digit OTP!")
                }
                else{
                    checkOTP(mobile,etOTP.text.toString().trim())
                }
            }
        }
    }


    fun checkOTP(mobile:String,OTP:String){
        val mProgressDialog = indeterminateProgressDialog("Please Wait", "Synchronization")
        mProgressDialog.show()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_CHECK_OTP,
            Response.Listener<String> { response ->
                try {
                    mProgressDialog.dismiss()
                    val obj = JSONObject(response)
                    if(obj.getBoolean("error")){
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                        sm!!.getInstance(this@OTPActivity).userLogin(obj.getString("name"),obj.getString("mobile"))
                        val intent = Intent(this,HomeActivity::class.java)
                        intent.putExtra("name",obj.getString("name"))
                        intent.putExtra("mobile",obj.getString("mobile"))
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: JSONException) {
                    mProgressDialog.dismiss()
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                    mProgressDialog.dismiss()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("mobile", mobile)
                params.put("otp", OTP)
                return params
            }
        }
        queue.add(stringRequest)
    }
}
