/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {
    // At the top of the GameFragment class, add a property of type GameViewModel
    // Initialize the GameViewModel using the by viewModels() Kotlin PROPERTY DELEGATE.
    private val viewModel: GameViewModel by viewModels()


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = GameFragmentBinding.inflate(inflater, container, false)
        // In GameFragment inside onCreateView(), after you get a reference to the binding object,
        // add a log statement to log the creation of the fragment.
        Log.d("GameFragment", "GameFragment created or recreated!")
        // In GameFragment inside onCreateView(), above the return statement add another
        // log to print the app data, word, score, and word count.
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        // updateNextWordOnScreen()
        // binding.score.text = getString(R.string.score, 0)
        // binding.wordCount.text = getString(
        //        R.string.word_count, 0, MAX_NO_OF_WORDS)
        viewModel.currentScrambledWord.observe(viewLifecycleOwner,
            { newWord ->
                binding.textViewUnscrambledWord.text = newWord
            })
    // In the GameFragment at the end of onViewCreated() method, attach observer for score. Pass
    // in the viewLifecycleOwner as the first parameter to the observer and a lambda expression for
    // the second parameter. Inside the lambda expression, pass the new score as a parameter and
    // inside the function body, set the new score to the text view.
        viewModel.score.observe(viewLifecycleOwner,
            { newScore ->
                binding.score.text = getString(R.string.score, newScore)
            })

        // At the end of the onViewCreated() method, attach an observer for the currentWordCount
        // LiveData. Pass in the viewLifecycleOwner as the first parameter to the observer and a
        // lambda expression for the second parameter. Inside the lambda expression, pass the new
        // word count as a parameter and in the function body, set the new word count along with
        // the MAX_NO_OF_WORDS to the text view.
        viewModel.currentWordCount.observe(viewLifecycleOwner,
            { newWordCount ->
                binding.wordCount.text =
                    getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
            })

    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    // Add a check on the return value of viewModel.nextWord() method. If true, another word
    // is available, so update the scrambled word on screen using updateNextWordOnScreen().
    // Otherwise the game is over, so display the alert dialog with the final score.
    private fun onSubmitWord()
    {
        // In GameFragment, at the beginning of onSubmitWord(), create a val called playerWord.
        // Store the player's word in it, by extracting it from the text field in the
        // binding variable.
        val playerWord = binding.textInputEditText.text.toString()
        // In onSubmitWord(), below the declaration of playerWord, validate the player's word.
        // Add an if statement to check the player's word using the isUserWordCorrect()
        // method, passing in the playerWord.
        if(viewModel.isUserWordCorrect(playerWord)) {
            // Inside the if block, reset the text field, call setErrorTextField passing in false.
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }


    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    // Similar to onSubmitWord(), add a condition in the onSkipWord() method. If true,
    // display the word on screen and reset the text field. If false and there's no more
    // words left in this round, show the alert dialog with the final score.
    private fun onSkipWord()
    {
        if(viewModel.nextWord())
        {
            setErrorTextField(false)
            // updateNextWordOnScreen()
        } else
        {
            showFinalScoreDialog()
        }
    }

    /*
     * Gets a random word for the list of words and shuffles the letters in it.
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    // In GameFragment, add a private function called showFinalScoreDialog(). To create a
    // MaterialAlertDialog, use the MaterialAlertDialogBuilder class to build up parts of the
    // dialog step-by-step. Call the MaterialAlertDialogBuilder constructor passing in the content
    // using the fragment's requireContext() method. The requireContext() method returns a
    // non-null Context.
    private fun showFinalScoreDialog()
    {
        MaterialAlertDialogBuilder(requireContext())
            // Add the code to set the title on the alert dialog, use a string
            // resource from strings.xml.
            .setTitle(getString(R.string.congratulations))
            // Set the message to show the final score, use the read-only version of the
            // score variable (viewModel.score), you added earlier.

            // In GameFragment, access the value of score using the value property. Inside the
            // showFinalScoreDialog() method, change viewModel.score to viewModel.score.value.
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            // Make your alert dialog not cancelable when the back key is pressed, using
            // setCancelable() method and passing false.
            .setCancelable(false)
            // Add two text buttons EXIT and PLAY AGAIN using the methods setNegativeButton()
            // and setPositiveButton(). Call exitGame() and restartGame() respectively
            // from the lambdas.
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again))
            {
                    _, _ ->
                restartGame()
            }
            .show()
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
        // updateNextWordOnScreen()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
     * Displays the next scrambled word on screen.
     */
    // GameFragment, update the method updateNextWordOnScreen() to use the read-only viewModel
    // property, currentScrambledWord

    // In GameFragment, delete the method updateNextWordOnScreen() and all the calls to it.
    // You do not require this method, as you will be attaching an observer to the LiveData.
    // private fun updateNextWordOnScreen() {
    //     binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    // }

    // In GameFragment, override the onDetach() callback method, which will be called when the
    // corresponding activity and fragment are destroyed.
    override fun onDetach(){
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }
}
