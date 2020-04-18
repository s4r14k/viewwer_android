package com.priscilla.viewwer.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private Context context;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * constructor
     *
     * @param context
     */
    public InputValidation(Context context) {
        this.context = context;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass
    // ===========================================================

    // ===========================================================
    // Methods for Interfaces
    // ===========================================================

    // ===========================================================
    // Public Methods
    // ===========================================================

    public boolean isInputEditTextNotNumber(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        String regexForNumber = "^[0-9-]+$";
        if (!(value.matches(regexForNumber))) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return  true;
    }

    public boolean isInputPasswordAccept(TextInputEditText textInputEditText,TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\da-zA-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
       // matcher = pattern.matcher(PASSWORD_PATTERN);

        if(!pattern.matcher(value).matches()){
            textInputLayout.setError(message);
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return  true;
    }

    public boolean isInputEditTextNotDouble(TextInputEditText textInputEditText) {
        String value = textInputEditText.getText().toString().trim();
        String regexForNumber = "^[0-9]+([\\,\\.][0-9]+)?$";
      //  String regexForNumber = "^[0-9-]+$";
        if (!(value.matches(regexForNumber))) {
            return false;
        }
        return  true;
    }

    public boolean isInputEditTextNotLength(int nbrLength, TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length() != nbrLength) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return  true;
    }

    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFill(EditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isInputEditTextFill(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputEditText.setError(message);
            textInputEditText.requestFocus();
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputEditText.setError(null);
        }

        return true;
    }

    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param inputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFill(EditText textInputEditText, EditText inputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            inputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            inputLayout.setError(message);
        }

        return true;
    }




    /**
     * method to check InputEditText has valid email .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextEmail (EditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextEmail(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputEditText.setError(message);
            textInputEditText.requestFocus();
            //textInputEditText.setFocusable(true);
          //  hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputEditText.setError(null);
        }

        return true;
    }
    /**
     * method to check InputEditText has valid email .
     *
     * @param textInputEditText
     * @param message
     * @return
     */
    public boolean isInputEmail(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputEditText.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputEditText.setError(message);
        }
        return true;
    }

//    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
//        String value1 = textInputEditText1.getText().toString().trim();
//        String value2 = textInputEditText2.getText().toString().trim();
//        if (!value1.contentEquals(value2)) {
//            textInputLayout.setError(message);
//            hideKeyboardFrom(textInputEditText2);
//            return false;
//        } else {
//            textInputLayout.setErrorEnabled(false);
//        }
//        return true;
//    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2,TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * method to Show keyboard
     *
     * @param view
     */
    private void showKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * method to Hide keyboard
     *
     * @param view
     */
    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


        public  double ParseDouble(String strNumber) {
            if (strNumber != null && strNumber.length() > 0) {
                try {
                    double res = Double.parseDouble(strNumber);
                   // textInputLayout.setErrorEnabled(false);
                    return res;
                } catch(Exception e) {
                  //  textInputLayout.setError("erreur de format");
                 //   hideKeyboardFrom(textInputEditText);
                    return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
                }
            }
            else {
              //  textInputLayout.setErrorEnabled(false);
                return 0;
            }
        }

}
