package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Change GameViewModel to be subclassed from ViewModel.
// ViewModel is an abstract class, so you need to extend it to use it in your app.
class GameViewModel : ViewModel()
{
    // Move the data variables score, currentWordCount, currentScrambledWord to GameViewModel class.

    // First add a backing property to the score variable. In GameViewModel, change the score
    // variable declaration so it utilizes the backing property
    private var _score = 0
    val score: Int
        get() = _score
    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount
    // For getter and setter methods, you could override one or both of these methods and
    // provide your own custom behavior. To implement a backing property, you will
    // override the getter method to return a read-only version of your data.

    // In GameViewModel change the currentScrambledWord declaration to add a backing property.
    // Now _currentScrambledWord is accessible and editable only within the GameViewModel.
    // The UI controller, GameFragment can read its value using the read-only property,
    // currentScrambledWord.
    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    // add a new class variable of type MutableList<String> called wordsList, to
    // hold a list of words you use in the game, to avoid repetitions.
    private var wordsList: MutableList<String> = mutableListOf()
    // Add another class variable called currentWord to hold the word the player is trying to
    // unscramble. Use the lateinit keyword since you will initialize this property later.
    private lateinit var currentWord: String

    // In GameViewModel.kt add an init block with a log statement.
    init
    {
        Log.d("GameFragment", "GameViewModel created!")
        // To display a scrambled word at the start of the app, you need to call the getNextWord()
        // method, which in turn updates currentScrambledWord. Make a call to the method
        // getNextWord() inside the init block of the GameViewModel.
        getNextWord()
    }

    // In the GameViewModel class, override the onCleared() method. The ViewModel
    // is destroyed when the associated fragment is detached, or when the activity is finished.
    // Right before the ViewModel is destroyed, the onCleared() callback is called.
    override fun onCleared()
    {
        super.onCleared()
        // Add a log statement inside onCleared() to track the GameViewModel lifecycle.
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    // Add a new private method called getNextWord(), above the init block,
    // with no parameters that returns nothing.
    private fun getNextWord()
    {
        //Get a random word from the allWordsList and assign it to currentWord
        currentWord = allWordsList.random()
        // convert the currentWord string to an array of characters and assign it
        // to a new val called tempWord
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        // Add the following while loop around the call to shuffle, to continue the loop until
        // the scrambled word is not the same as the original word.
        while (String(tempWord).equals(currentWord, false))
        {
            // To scramble the word, shuffle characters in this array using the Kotlin method, shuffle().
            tempWord.shuffle()
        }
        // Add an if-else block to check if a word has been used already. If the wordsList
    // contains currentWord, call getNextWord(). If not, update the value of _currentScrambledWord
    // with the newly scrambled word, increase the word count, and add the new word to the wordsList.
        if (wordsList.contains(currentWord))
        {
            getNextWord()
        } else
        {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }

    }

    // In GameViewModel, add a new private method called increaseScore() with no parameters and
    // no return value. Increase the score variable by SCORE_INCREASE.
    private fun increaseScore()
    {
        _score += SCORE_INCREASE
    }

    // In GameViewModel, add a helper method called isUserWordCorrect() which returns a Boolean
    // and takes a String, the player's word, as a parameter.
    fun isUserWordCorrect(playerWord: String): Boolean
    {
        // In isUserWordCorrect() validate the player's word and increase the score if the guess
        // is correct. This will update the final score in your alert dialog
        if(playerWord.equals(currentWord, true))
        {
            increaseScore()
            return true
        }
        return false
    }

// In the GameViewModel class, add another method called nextWord(). Get the next word from
// the list and return true if the word count is less than the MAX_NO_OF_WORDS.
    fun nextWord(): Boolean
    {
        return if (currentWordCount <  MAX_NO_OF_WORDS)
        {
            getNextWord()
            true
        } else false
    }



}