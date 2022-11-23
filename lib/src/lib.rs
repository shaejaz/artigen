extern crate jni;

use std::ffi::CString;
use std::os::raw::c_char;

use jni::JNIEnv;
use jni::objects::{JClass, JList, JObject, JValue};
use jni::sys::{jint, jintArray, jlong, jobject};
use jni::signature::{ReturnType, Primitive};

pub type Callback = unsafe extern "C" fn(*const c_char) -> ();

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn invokeCallbackViaJNA(callback: Callback) {
    let s = CString::new("Hello from Rust").unwrap();
    unsafe { callback(s.as_ptr()); }
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_com_example_artigen_MainActivity_invokeCallbackViaJNI(
    env: JNIEnv,
    _class: JClass
) -> jobject {
    let list_object = env.new_object(
        "java/util/ArrayList",
        "(I)V",
        &[JValue::Int(5 as i32)]).expect("TODO: panic message"
    );
    let list = JList::from_env(&env, list_object).expect("TODO: panic message");

    for i in 0..2 {
        let arr = env.new_int_array(3).expect("test");
        let norm = i * 3;

        let el1 = 1 + norm;
        let el2 = 2 + norm;
        let el3 = 3 + norm;
        let buf = &[el1, el2, el3];

        env.set_int_array_region(arr, 0, buf).expect("TODO: panic message");
        let mut new_obj = JObject::null();
        unsafe {
            new_obj = JObject::from_raw(arr);
        }

        list.add(new_obj).expect("TODO: panic message");
    }

    list.into_raw()
}