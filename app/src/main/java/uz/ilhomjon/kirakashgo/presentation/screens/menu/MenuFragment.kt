package uz.ilhomjon.kirakashgo.presentation.screens.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private val binding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
}