package com.example.masterclassquizcompanion

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {



    private var mCurrentPosition: Int = 1
    private var mQuestions: ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        mQuestions = Constants.getQuestions()

        setQuestion()
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)


    }

    private fun defaultOptionsView(){
        var options = arrayListOf<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for(option in options){

            option.setTextColor(Color.parseColor("#ffffff"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,
                    R.drawable.default_option_border)
        }

    }

    private fun setQuestion(){


        val question = mQuestions!!.get(mCurrentPosition - 1)
        defaultOptionsView()

        btn_submit.text = "SUBMIT"


        tv_question_num.text = "Question " + mCurrentPosition

        progress_bar.progress = mCurrentPosition - 1
        tv_progress.text = "$mCurrentPosition" +  "/" + progress_bar.max

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour

    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.tv_option_one -> {
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_option_four, 4)
            }
            R.id.btn_submit -> {
            if(mSelectedOptionPosition == 0){
                mCurrentPosition++

                when{
                    mCurrentPosition <= mQuestions!!.size -> {
                        setQuestion()
                    }else -> {
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(Constants.USER_NAME, mUserName)
                    intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                    intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestions!!.size)
                    startActivity(intent)
                }



                }

            }else{
                val question = mQuestions?.get(mCurrentPosition -1)
                if(question!!.correctAnswer  != mSelectedOptionPosition){
                    answerView(mSelectedOptionPosition, R.drawable.incorrect_option_border)
                }else{
                    mCorrectAnswers++
                }

                answerView(question.correctAnswer, R.drawable.correct_option_border)

                if(mCurrentPosition == mQuestions!!.size){
                    btn_submit.text = "FINISH"

                }else{
                    btn_submit.text = "NEXT QUESTION"
                }

                mSelectedOptionPosition = 0


            }


            }
        }
    }

    private fun answerView (answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }

        }

    }


    private fun selectedOptionView(tv:TextView, selectedOptionNum: Int){
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#000000"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,
                R.drawable.selected_option_border)

    }
}