# CustomEditText

A very simple and light weighted editbox for android.

# Usage

Step 1 : Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2 : Add the dependency

	dependencies {
	        implementation 'com.github.rkmobile2519:customedittext:Tag'
	}


Step 3 : Add edittext to your xml layout.

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="@color/colorPrimaryDark"
      android:orientation="vertical"
      android:padding="@dimen/_10sdp"
      tools:context=".MainActivity">

      <com.lib.customedittext.CustomEditText
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_margin="@dimen/_8sdp"
          android:imeOptions="actionNext"
          android:inputType="text"
          android:maxLength="15"
          app:edt_background="@drawable/round_editbox"
          app:edt_hint="Enter Name"
          app:edt_padding="@dimen/_10sdp"
          app:edt_text_color="@android:color/black"
          app:edt_text_size="@dimen/_12ssp" />

    </LinearLayout>
    
    You can also change attributes for Custom EditText Programatically.
	
	
# Custom Attributes

    
| Attributes | Format | Example |
| :---         |     :---      |          :--- |
| edt_drawable_start   | reference     |  app:edt_background="@drawable/round_editbox"    |
| edt_cursor   | reference     | app:edt_cursor="@drawable/cursor"      |   
| edt_padding   | reference\|dimension     | app:edt_padding="5dp"      | 
| edt_padding_start   | reference\|dimension     | app:edt_padding_start="8dp"      | 
| edt_padding_end   | reference\|dimension     | app:edt_padding_end="8dp"      | 
| edt_padding_top   | reference\|dimension     | app:edt_padding_top="8dp"      | 
| edt_padding_bottom   | reference\|dimension     | app:edt_padding_bottom="8dp"      | 
| edt_background   | reference     | app:edt_background="@drawable/bg"      | 
| edt_background_tint   | reference\|color     | app:edt_background_tint="#fffffff"      | 
| edt_hint   | string     | app:edt_hint="Enter Name"      | 
| edt_text_color   | color     | app:edt_text_color="#333333"      | 
| edt_toggle_text_color   | color    | app:edt_toggle_text_color="#333333"      | 
| edt_text_hint_color   | reference\|color     | app:edt_text_hint_color="#dedede"      |  
| edt_image_tint   | reference\|color     | app:edt_image_tint="#000000"      | 
| edt_text_size   | reference\|dimension     | app:edt_text_size="14sp"      | 
| edt_editable   | boolean     | app:edt_editable="false"      | 
| edt_show_drawable   | boolean     | app:edt_show_drawable="false"      | 
| edt_password_toggle   | boolean     | app:edt_password_toggle="true"      | 
 
 
 # Contact
 
 Feel free to contact for any question or bugs:
 
 * Email : rkmobile2519@gmail.com
 
 
# Licence

Copyright 2019 RKMobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License
