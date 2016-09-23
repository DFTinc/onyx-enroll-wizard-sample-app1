# onyx-enroll-wizard-sample-app1

Getting Started
---------------

If you don't already have Android Studio, you can download it <a href="http://developer.android.com/sdk/index.html" target="_blank">here</a>.

Once Android Studio is installed, please contact DFT to purchase your ONYX license <a href="http://www.diamondfortress.com/contact" target="_blank">here</a>. <br />
**Note: Make sure you have updated to the latest Android SDK via the SDK Manager.**

You should receive a license key of the form XXXX-XXXX-XXXX-X-X at your provided e-mail address.
<br />
Next, you can clone our sample repository on the command-line using the following commands:

    > cd <YOUR_DEVELOPMENT_ROOT>
    > git clone https://github.com/DFTinc/onyx-enroll-wizard-sample-app1.git
    
Alternatively, you can clone the project via Android Studio:
<br/>
Select `VCS >> Checkout from Version Control >> GitHub`, and follow the on-screen instructions.

Right click on `onyxenrollwizardsampleapp/src/main/res/values` and select 
"New >> Values resource file". Name the file `license.xml`, and place the following contents into
this file as shown below:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <!-- Replace this string with your ONYX license key -->
        <string name="license_key_value">YOUR-LICENSE-KEY-HERE</string>
    </resources>
    
license.xml will be automatically ignored from the git repository, so you don't need to worry about
your license becoming publicly accessible.

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Build >> Make Project" in Android Studio.

Now plug in your compatible device, and select Run >> Run 'onyxenrollwizardsampleapp'.

Support
-------

- Diamond Fortress Technologies Support Site: <a href="http://support.diamondfortress.com" target="_blank">support.diamondfortress.com</a>
