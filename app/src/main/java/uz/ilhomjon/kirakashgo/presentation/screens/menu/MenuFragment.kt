package uz.ilhomjon.kirakashgo.presentation.screens.menu

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentMenuBinding
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MenuFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    private val viewModel:DriverProfileViewModel by viewModels()
    private lateinit var progresDialog:ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MySharedPreference.init(binding.root.context)
        val apiKey = MySharedPreference.token.access
        progresDialog = ProgressDialog(context)
//        val apiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyMzU0ODI2LCJpYXQiOjE3MDA4MTg4MjYsImp0aSI6Ijg3ZDc5NDNjMDk5ODQ2ODNiNjA5OTQwMDhmYjExYmMxIiwidXNlcl9pZCI6MTd9.D-8ZUxZt7hyxV_wbGpeNHpbjWlHgZRyyk0I1bEPM4W0"
        launch(Dispatchers.Main) {
            viewModel.getDriverProfile(apiKey =apiKey).collectLatest {
                when(it?.status){
                    Status.LOADING->{
                        progresDialog.setTitle("Yuklanmoqda")
                        progresDialog.setMessage(it.message)
                        progresDialog.show()
                    }
                    Status.ERROR->{
                        progresDialog.hide()
                        val dialog = AlertDialog.Builder(binding.root.context)
                        dialog.setTitle("Xatolik")
                        dialog.setMessage(it.message)
                        dialog.show()
                    }
                    Status.SUCCESS->{
                        binding.workerName.text = "${it.data?.data?.first_name} ${it.data?.data?.last_name}"
                        binding.accountBalance.text = "${it.data?.data?.balance} so'm"
                        binding.infoTitle.text = "${it.data?.data?.car_type}"
                        binding.infoNumber.text = it.data?.data?.car_number
                        binding.infoType.text = it.data?.data?.category?.type
                        binding.noteBalance.text = it.data?.data?.phone
                    }
                    else-> Toast.makeText(
                        context,
                        "Xatolik yuz berdi va uni topa olmadik",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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