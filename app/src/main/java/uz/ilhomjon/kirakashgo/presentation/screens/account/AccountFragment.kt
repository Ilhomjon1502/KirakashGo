package uz.ilhomjon.kirakashgo.presentation.screens.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private val binding by lazy { FragmentAccountBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}