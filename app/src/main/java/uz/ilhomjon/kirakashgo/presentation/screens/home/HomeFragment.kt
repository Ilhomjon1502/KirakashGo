package uz.ilhomjon.kirakashgo.presentation.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.menuBtn.setOnClickListener {
            findNavController().navigate(R.id.menuFragment, bundleOf("1" to 1), navOption.build())
        }
        binding.aboutBtn.setOnClickListener {
            findNavController().navigate(R.id.driverFragment, bundleOf("1" to 1), navOption.build())
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        binding.cashTv.startAnimation(animation)
        binding.aboutBtn.startAnimation(animation)
    }
}