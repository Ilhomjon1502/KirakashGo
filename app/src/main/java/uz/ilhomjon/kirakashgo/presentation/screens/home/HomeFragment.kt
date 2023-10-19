package uz.ilhomjon.kirakashgo.presentation.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.menuBtn.setOnClickListener {
            findNavController().navigate(R.id.menuFragment)
        }
        binding.aboutBtn.setOnClickListener {
            findNavController().navigate(R.id.driverFragment)
        }
        return binding.root
    }
}