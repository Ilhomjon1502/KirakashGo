package uz.ilhomjon.kirakashgo.presentation.screens.checksmscode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentCheckSmsCodeBinding
import uz.ilhomjon.kirakashgo.presentation.common.CustomOtpView
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class CheckSmsCodeFragment : Fragment(), CoroutineScope {
    private val binding by lazy { FragmentCheckSmsCodeBinding.inflate(layoutInflater) }
    private val driverViewModel: DriverViewModel by viewModels()
    private lateinit var username: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString("username") as String
        Log.d("Test", "onViewCreated: $username")

        binding.nextBtn.setOnClickListener {
            val customOtpView = requireView().findViewById<CustomOtpView>(R.id.customOtpView)
            val otp = customOtpView.getOtp()
            Log.d("Test", "onViewCreated: $otp")

            MySharedPreference.init(binding.root.context)

            val navOption = NavOptions.Builder()
            navOption.setEnterAnim(R.anim.ochilish_1)
            navOption.setPopEnterAnim(R.anim.ochilish_2)
            navOption.setExitAnim(R.anim.yopilish_2)
            navOption.setPopExitAnim(R.anim.yopilish_1)


            launch(Dispatchers.Main) {
                driverViewModel.smsCheckCode("$username", "$otp")
                    .collectLatest { checkSmsResponse ->
                        if (checkSmsResponse!!.success!!) {
                            driverViewModel.getDriverToken("$username").collectLatest { token ->
                                if (token != null) {
                                    MySharedPreference.token = token
                                    findNavController().navigate(
                                        R.id.homeFragment, bundleOf("1" to 1), navOption.build()
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}