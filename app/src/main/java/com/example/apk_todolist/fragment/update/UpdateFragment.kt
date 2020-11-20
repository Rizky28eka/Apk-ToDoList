package com.example.apk_todolist.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apk_todolist.R
import com.example.apk_todolist.data.models.ToDoData
import com.example.apk_todolist.data.viewmodel.ToDoViewModel
import com.example.apk_todolist.databinding.FragmentUpdateBinding
import com.idn.todoapp.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding?.args = args

        setHasOptionsMenu(true)

        return binding?.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmDeleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteItem() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete '${args.currentItem.title}")
            .setMessage("Are you sure want to remo '${args.currentItem.title}")
            .setPositiveButton("Yes") { _, _ ->
                mToDoViewModel.deleteData(args.currentItem)
                Toast.makeText(requireContext(), "Successfully Removed", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            .setNegativeButton("NO", null)
            .create()
            .show()
    }

    private fun updateItem() {
        val title = edt_title_current.text.toString()
        val description = edt_description_current.text.toString()
        val getPriority = spinner_priorities_current.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title,description)
        if (validation){
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )
            mToDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(), "Succesfully Updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please Fill out all the field", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}