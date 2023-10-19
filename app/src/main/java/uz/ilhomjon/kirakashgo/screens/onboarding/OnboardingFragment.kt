package uz.ilhomjon.kirakashgo.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private val binding by lazy { FragmentOnboardingBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        return binding.root
    }
}