package uz.ilhomjon.kirakashgo.presentation.screens.onboarding

import android.content.SharedPreferences
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentOnboardingBinding
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private val binding by lazy { FragmentOnboardingBinding.inflate(layoutInflater) }
    private val driverViewModel: DriverViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        MySharedPreference.init(binding.root.context)
        val token = MySharedPreference.token
        Log.d("Test", "onCreateView: $token")

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.nextBtn.setOnClickListener {
            if (token.access == null) {
                findNavController().navigate(
                    R.id.registerFragment,
                    bundleOf("1" to 1),
                    navOption.build()
                )
            } else {
                findNavController().navigate(
                    R.id.homeFragment,
                    bundleOf("1" to 1),
                    navOption.build()
                )
            }
        }
        return binding.root
    }
}