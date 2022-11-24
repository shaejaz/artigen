extern crate jni;

use std::ffi::CString;
use std::os::raw::c_char;
use rand::Rng;

use jni::JNIEnv;
use jni::objects::{JClass, JList, JObject, JValue};
use jni::sys::{jint, jintArray, jlong, jobject};
use jni::signature::{ReturnType, Primitive};

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
    let main_list = JList::from_env(&env, list_object).expect("TODO: panic message");

    let x = 50;
    let y = 50;

    for i in 0..x {
        let row_object = env.new_object(
            "java/util/ArrayList",
            "(I)V",
            &[JValue::Int(5 as i32)]).expect("TODO: panic message"
        );
        let row = JList::from_env(&env, row_object).expect("TODO: panic message");

        for j in 0..y {
            let r = rand::thread_rng().gen_range(0..256);
            let g = rand::thread_rng().gen_range(0..256);
            let b = rand::thread_rng().gen_range(0..256);

            let r_obj = env.new_object("java/lang/Integer", "(I)V", &[JValue::Int(r)]).expect("test");
            let g_obj = env.new_object("java/lang/Integer", "(I)V", &[JValue::Int(g)]).expect("test");
            let b_obj = env.new_object("java/lang/Integer", "(I)V", &[JValue::Int(b)]).expect("test");

            let rgb_object = env.new_object(
                "java/util/ArrayList",
                "(I)V",
                &[JValue::Int(5 as i32)]).expect("TODO: panic message"
            );
            let rgb = JList::from_env(&env, rgb_object).expect("TODO: panic message");

            rgb.add(r_obj).expect("test");
            rgb.add(g_obj).expect("test");
            rgb.add(b_obj).expect("test");

            row.add(JObject::from(rgb)).expect("test")
        }

        main_list.add( JObject::from( row)).expect("test")
    }

    main_list.into_raw()
}