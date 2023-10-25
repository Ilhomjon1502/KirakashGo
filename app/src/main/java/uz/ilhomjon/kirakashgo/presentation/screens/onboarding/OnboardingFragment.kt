package uz.ilhomjon.kirakashgo.presentation.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private val binding by lazy { FragmentOnboardingBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                R.id.registerFragment,
                bundleOf("1" to 1),
                navOption.build()
            )
        }
        return binding.root
    }
}