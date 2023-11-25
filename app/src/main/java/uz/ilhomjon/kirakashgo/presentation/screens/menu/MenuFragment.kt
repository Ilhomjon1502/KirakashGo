package uz.ilhomjon.kirakashgo.presentation.screens.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentMenuBinding
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MenuFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    private val viewModel:DriverProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MySharedPreference.init(binding.root.context)
//        val apiKey = MySharedPreference.token.access
        val apiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyMjg1ODc0LCJpYXQiOjE3MDA3NDk4NzQsImp0aSI6IjMyZGQzNjBjN2JmYzQ5NDM5OTA3NmIyNDkwMTA4MjQyIiwidXNlcl9pZCI6MTV9.Clx7mV5ugojchjwvA_x3l5-jdnEAInKiO_Hrx6zfThk"
        launch {
            viewModel.getDriverProfile(apiKey =apiKey).collectLatest {
                Log.d("MenuFragmentDriverData", "onCreateView: $it")
            }
        }


        binding.accountCard.setOnClickListener {
            findNavController().navigate(R.id.accountFragment)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        binding.profilePhoto.startAnimation(animation)
        binding.infoCard.startAnimation(animation)
        binding.noteCard.startAnimation(animation)
        binding.accountCard.startAnimation(animation)
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}