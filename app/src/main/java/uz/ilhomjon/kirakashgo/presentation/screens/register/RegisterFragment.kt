package uz.ilhomjon.kirakashgo.presentation.screens.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RegisterFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val driverViewModel: DriverViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        try {
            binding.nextBtn.setOnClickListener {
                launch(Dispatchers.Main) {
                    try {
                        driverViewModel.loginDriver(binding.loginEdt.text.toString())
                            .collectLatest {
                                Log.d("Test", "onCreateView: $it")
                                if (it != null) {
                                    if (it.success) {
                                        findNavController().navigate(
                                            R.id.checkSmsCodeFragment,
                                            bundleOf("username" to binding.loginEdt.text.toString()),
                                            navOption.build()
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No user found for this username",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    } catch (e: IOException) {
                        Toast.makeText(
                            context,
                            "Internet bilan bog'liq xatolik",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Xatolik!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        } catch (e: IOException) {
            Toast.makeText(context, "Internet bilan aloqani tekshiring", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Xatolik!", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}