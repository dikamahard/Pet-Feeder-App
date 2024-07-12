package com.dikamahard.petfeeder

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dikamahard.petfeeder.databinding.FragmentHomeBinding
import com.dikamahard.petfeeder.databinding.FragmentRoleBinding
import com.dikamahard.petfeeder.helper.MQTTClient
import com.dikamahard.petfeeder.helper.StatePreferences
import com.dikamahard.petfeeder.helper.dataStore
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var statePreferences: StatePreferences
    private lateinit var mqttCLient:MqttAndroidClient
    val topic = "garudahacks/tes"
    val giveFoodTopic = "farfeed/give"
    val getFoodTopic = "farfeed/get"
    var currentWeigth = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statePreferences = StatePreferences.getInstance(requireContext().dataStore)

        // Get arguments passed by ConnectFragment
        //val serverURI   = "tcp://broker.hivemq.com:1883"
        val serverURI   = "tcp://203.194.113.47:1883"


        val clientId    = "qwerty123456"
        val username = "garudahacks"
        val password = "asustufgaming"
        //val message = "HALO FROM THE APP"

        // Open MQTT Broker communication
        val mqttClient = MQTTClient(requireContext(), serverURI, clientId)

        // Connect and login to MQTT Broker
        mqttClient.connect(
            username,
            password,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(this.javaClass.name, "Connection success")
                    Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()
                    mqttClient.subscribe(
                        "weightData",
                        1,
                        object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken?) {
                                val msg = "Subscribed to: weightData"
                                Log.d(this.javaClass.name, msg)
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                                Log.d(this.javaClass.name, "Failed to subscribe: $topic")
                            }
                        }
                    )
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

                    Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}", Toast.LENGTH_SHORT).show()

                    // Come back to Connect Fragment
                }
            },
            object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val msg = "Receive message: ${message.toString()} from topic: $topic"
                    Log.d(this.javaClass.name, msg)
                    binding.tvWeight.text = message.toString() + " Grams"
                    currentWeigth = message.toString().toInt()

                    //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d(this.javaClass.name, "Delivery complete")
                }
            }
        )



        binding.btnFood.setOnClickListener {
            if (mqttClient.isConnected()) {
                if (currentWeigth < binding.textInputEditText.text.toString().toInt()) {
                    Toast.makeText(requireContext(),"Food Not Enough", Toast.LENGTH_SHORT).show()
                }else {
                    Log.d("TAG", "btnfood clicked")
                    mqttClient.publish(
                        topic,
                        binding.textInputEditText.text.toString(),
                        1,
                        false,
                        object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken?) {
                                val msg ="Publish message:  to topic: $topic"
                                Log.d(this.javaClass.name, msg)

                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                                Log.d(this.javaClass.name, "Failed to publish message to topic")
                            }
                        })
                }
            }else {
                Toast.makeText(requireContext(), "Not Connected", Toast.LENGTH_SHORT).show()
            }

        }

        /*
        if (mqttClient.isConnected()) {
            mqttClient.subscribe(
                "weightData",
                1,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        val msg = "Subscribed to: weightData"
                        Log.d(this.javaClass.name, msg)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Failed to subscribe: $topic")
                    }
                }
            )
        }

         */

        /*

        binding.btnTes.setOnClickListener {
            lifecycleScope.launch {
                val role = statePreferences.getRole("role_chosen_key")

                Log.d("TAG", "role: $role")
                Log.d("TAG", "role state: ${statePreferences.getState("role_state_key")}")
                Log.d("TAG", "onboarding state: ${statePreferences.getState("onboarding_state_key")}")
            }
            // Connect and login to MQTT Broker
            mqttClient.connect(
                username,
                password,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(this.javaClass.name, "Connection success")

                        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()

                        // TODO: PUBLISH
//                        mqttClient.publish(
//                            topic,
//                            message,
//                            1,
//                            false,
//                            object : IMqttActionListener {
//                                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                                    val msg ="Publish message: $message to topic: $topic"
//                                    Log.d(this.javaClass.name, msg)
//
//                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//                                }
//
//                                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
//                                }
//                            })

                        mqttClient.subscribe(
                            "weightData",
                            1,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "Subscribed to: weightData"
                                    Log.d(this.javaClass.name, msg)


                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                                    Log.d(this.javaClass.name, "Failed to subscribe: $topic")
                                }
                            }
                        )

                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

                        Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}", Toast.LENGTH_SHORT).show()

                        // Come back to Connect Fragment
                    }
                },
                object : MqttCallback {
                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val msg = "Receive message: ${message.toString()} from topic: $topic"
                        Log.d(this.javaClass.name, msg)
                        binding.tvWeight.text = message.toString() + " Grams"

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun connectionLost(cause: Throwable?) {
                        Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {
                        Log.d(this.javaClass.name, "Delivery complete")
                    }
                }
            )



            //connectMqtt()
            //publish("halo from app")


        }

         */

    }
/*
    fun connectMqtt() {
        var clientId = MqttClient.generateClientId()
        mqttCLient = MqttAndroidClient(requireContext(),       "tcp://broker.hivemq.com:1883", clientId)

//        val options = MqttConnectOptions()
//        options.userName = "garudahacks"
//        options.password = "asustufgaming".toCharArray()

        try {
            var token: IMqttToken = mqttCLient.connect()

            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "connection success")
                    //subscribe()
                    //publish("halo from the app")
                }
                override fun onFailure(asyncActionToken: IMqttToken?,    exception: Throwable?) {
                    Log.d("MQTT", "connection failure ${exception?.message} casued by ${exception?.cause}")
                }
            }

            mqttCLient.setCallback( object: MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.d("MQTT","connection lost")
                }
                override fun messageArrived(topic: String?, message:  MqttMessage?) {
                    Log.d("MQTT", "message arrived on topic "+topic+ "  message:"+message?.toString())
                }
                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MQTT", "deliveryComplete")
                }
            })
        } catch (e: MqttException) {
            Log.e("MQTT", "${e.message}")

        }
    }

 */

    /*
    fun subscribe(){
        var token=mqttCLient.subscribe(topic,1)
        token.actionCallback=object:IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "subscribe success")
            }
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "subscribe failed")
            }
        }
    }

    fun publish(payload:String){
        val message = MqttMessage()
        message.payload = payload.toByteArray()
        message.qos = 1
        message.isRetained = false
        mqttCLient.publish(topic, message, null,  object:IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "publish success")
            }
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "publish failed")
            }
        })
    }

     */
}