package uz.ilhomjon.kirakashgo.presentation.screens.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import uz.ilhomjon.kirakashgo.databinding.FragmentRegisterBinding
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RegisterFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val driverViewModel: DriverViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.callNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${getString(R.string.call_center_raqami)}")
            startActivity(intent)
        }

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.nextBtn.setOnClickListener {
            launch(Dispatchers.Main) {
                driverViewModel.loginDriver(binding.loginEdt.text.toString())
                    .collectLatest {
                        Log.d("Test", "onCreateView: $it")
                        when(it?.status){
                            Status.SUCCESS->{
                                findNavController().popBackStack()
                                findNavController().navigate(
                                    R.id.checkSmsCodeFragment,
                                    bundleOf("username" to binding.loginEdt.text.toString()),
                                    navOption.build()
                                )
                            }
                            Status.LOADING->{
                                binding.nextBtn.isEnabled = false
                                binding.nextBtn.text = it.message
                                binding.progressBar.visibility=View.VISIBLE
                            }
                            Status.ERROR->{
                                binding.nextBtn.isEnabled = true
                                binding.nextBtn.text = "Kirish"
                                binding.progressBar.visibility=View.INVISIBLE
                                val dialog = AlertDialog.Builder(binding.root.context)
                                dialog.setTitle("Xatolik")
                                dialog.setMessage(it.message)
                                dialog.show()
                            }
                            else->{
                                Toast.makeText(
                                    context,
                                    "Muammo nimada ekanlgini topa olmadik, Error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

            }
        }


        return binding.root
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}