package videola.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import io.videola.sdk.Videola
import io.videola.sdk.model.CallEndReason
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class MainActivity : AppCompatActivity() {
    private val REQUEST_CALL = 1
    private val SIGNER_URL = "https://demo.videola.io/signer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_video_button.setOnClickListener {
            initCall(true)
        }

        main_voice_button.setOnClickListener {
            initCall(false)
        }
    }

    private fun initCall(videoCall: Boolean) {
        val otherExtras = Bundle().apply {
            putBoolean(Videola.GRV_EXTRA_VIBRATE_IN_CHAT, false)
            putBoolean(Videola.GRV_EXTRA_DISABLE_CHAT, false)
        }

        val code = main_edittext.text.toString()
        val result = Videola.Builder(this)
                .callCode(code)
                .videoCall(videoCall)
                .clientId("demo")
                .requestCode(REQUEST_CALL)
                .otherExtras(otherExtras)
                .eventsListener(eventsListener)
                .build()

        when (result) {
            Videola.GRV_INIT_MISSING_CALL_CODE -> { }
            Videola.GRV_INIT_INVALID_CALL_CODE -> { }
            Videola.GRV_INIT_MISSING_CLIENT_ID -> { }
            Videola.GRV_INIT_OFFLINE -> { }
            else -> { }
        }
    }

    private val eventsListener = object : Videola.EventsListener {
        override fun callInit(videoCall: Boolean, code: String) {
        }

        override fun requestToSignApiAuthToken(token: String) {
            Videola.authorize(signToken(token))
        }

        override fun callEstablished(code: String) {
        }

        override fun callEnd(data: Intent, isInForeground: Boolean) {
            parseCallExtras(data)
        }

        override fun recordingStateChanged(us: Boolean, them: Boolean) {
        }

        override fun recordingFilename(filename: String) {
        }
    }

    private fun signToken(token: String): String {
        val body = RequestBody.create(MediaType.parse("text/plain"), token)
        val request = Request.Builder()
                .url(SIGNER_URL)
                .post(body)
                .build()

        val response = OkHttpClient().newCall(request).execute()
        return response.body()?.string() ?: ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CALL && resultCode == Activity.RESULT_OK && data != null) {
            parseCallExtras(data)
        }
    }

    private fun parseCallExtras(data: Intent) {
        val endReason = data.getSerializableExtra(Videola.GRV_RES_CALL_END_REASON)
        val callCode = data.getStringExtra(Videola.GRV_RES_CALL_CODE)
        val leftMessageTo = data.getStringExtra(Videola.GRV_RES_LEFT_MESSAGE_TO)
        val duration = data.getIntExtra(Videola.GRV_RES_CALL_DURATION, 0)
        val messagesSent = data.getIntExtra(Videola.GRV_RES_MESSAGES_SENT, 0)

        when (endReason as CallEndReason) {
            CallEndReason.BUSY -> { }
            CallEndReason.HANDLE_BUSY -> {}
            CallEndReason.HANDLE_UNREACHABLE -> { }
            CallEndReason.HANDLE_NONEXIST -> { }
            CallEndReason.FREE_DEMO_ENDED -> { }
            CallEndReason.ROOM_LIMIT_REACHED -> { }
            CallEndReason.NO_CONNECTION -> { }
            CallEndReason.INVALID_CREDENTIALS -> { }
            CallEndReason.UNSUPPORTED_PROTOCOL_VERSION -> { }
            CallEndReason.OTHER_PARTY -> { }
            else -> { }     // USER - we hanged up the call
        }
    }
}
