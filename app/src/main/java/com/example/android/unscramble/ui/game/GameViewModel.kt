package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // 1. In GameViewModel, change the type of the _score and _currentWordCount class variables to val.
    // 2. Change the data type of the variables _score and _currentWordCount to MutableLiveData and
    // initialize them to 0.
    // 3. Change backing fields type to LiveData<Int>.

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score
    // In GameViewModel, right click on the variable currentWordCount, select Refactor >
    // Rename... . Prefix the new name with an underscore, _currentWordCount
    // Add a backing field
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount
    // For getter and setter methods, you could override one or both of these methods and
    // provide your own custom behavior. To implement a backing property, you will
    // override the getter method to return a read-only version of your data.

    // In GameViewModel change the currentScrambledWord declaration to add a backing property.
    // Now _currentScrambledWord is accessible and editable only within the GameViewModel.
    // The UI controller, GameFragment can read its value using the read-only property,
    // currentScrambledWord.

    // In GameViewModel, change the type of the variable _currentScrambledWord to
    // MutableLiveData<String>. LiveData and MutableLiveData are generic classes, so you need
    // to specify the type of data that they hold.
    private val _currentScrambledWord = MutableLiveData<String>()
    // Change the backing field, currentScrambledWord type to LiveData<String>,
    // because it is immutable. Android Studio will show some errors which you will
    // fix in the next steps.
    val currentScrambledWord: LiveData<String>
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
            // To access the data within a LiveData object, use the value property. In GameViewModel
            // inside the getNextWord() method, within the else block, change the reference of
            // _currentScrambledWord to _currentScrambledWord.value.
            _currentScrambledWord.value = String(tempWord)
            // Similarly use inc() Kotlin function to increment the value by one with null-safety.
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }

    }

/*
* Re-initializes the game data to restart the game.
*/
    // To reset the app data, in GameViewModel add a method called reinitializeData().
    // Set the score and word count to 0. Clear the word list and call getNextWord() method.

    // In GameViewModel at the beginning of the reinitializeData() method, change the reference of
    // _score and _currentWordCount to _score.value and _currentWordCount.value respectively.
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    // In GameViewModel, add a new private method called increaseScore() with no parameters and
    // no return value. Increase the score variable by SCORE_INCREASE.

    // In GameViewModel, inside the increaseScore() and getNextWord() methods, change the
    // reference of _score and _currentWordCount to _score.value and _currentWordCount.value
    // respectively. Android Studio will show you an error because _score is no longer an integer,
    // it's LiveData, you will fix it in the next steps.

    private fun increaseScore()
    {
        // Use the plus() Kotlin function to increase the _score value, which performs the
        // addition with null-safety.
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
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

    // In the GameViewModel, inside the nextWord() method, change the reference of
    // _currentWordCount to _currentWordCount.value!!.
    fun nextWord(): Boolean
    {
        return if (currentWordCount.value!! <  MAX_NO_OF_WORDS)
        {
            getNextWord()
            true
        } else false
    }



}