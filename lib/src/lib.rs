use std::os::raw::{c_char};
use std::ffi::{CString, CStr};
use rand::Rng;

#[no_mangle]
pub extern fn rust_doing_something(to: *const c_char) -> *mut c_char {
    let c_str = unsafe { CStr::from_ptr(to) };
    let reciever = match c_str.to_str() {
        Err(_) => "Error",
        Ok(string) => string,
    };
    // Let's generate a random number from the said range below that can act as a code
    let num = rand::thread_rng().gen_range(1000..9999);
    let numString = num.to_string();

    CString::new(reciever.to_owned() + Box::leak(numString.into_boxed_str())).unwrap().into_raw()
}

#[cfg(target_os="android")]
#[allow(non_snake_case)]
pub mod android {
    extern crate jni;use super::*;
    use self::jni::JNIEnv;
    use self::jni::objects::{JClass, JString};
    use self::jni::sys::{jstring};// Access our RustBridge class doSomething function
    #[no_mangle]
    pub unsafe extern fn Java_com_example_artigen_RustBridge_invokeFunction(env: JNIEnv, _: JClass, java_pattern: JString) -> jstring {
        let info = rust_doing_something(env.get_string(java_pattern).expect("Invalid!").as_ptr());
        let info_pointer = CString::from_raw(info);
        let output = env.new_string(info_pointer.to_str().unwrap()).expect("Couldn't create string from Android Function!");

        output.into_inner()
    }
}