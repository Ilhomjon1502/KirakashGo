package uz.ilhomjon.kirakashgo.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.ilhomjon.kirakashgo.databinding.FragmentCheckSmsCodeBinding

class CheckSmsCodeFragment : Fragment() {

    private val binding by lazy { FragmentCheckSmsCodeBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {



        return binding.root
    }

}