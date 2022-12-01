extern crate jni;

use artigen::patterns::{julia, Pattern};
use artigen::output::base64;

use jni::JNIEnv;
use jni::objects::{JClass};
use jni::sys::{jstring};

fn generate_image() -> String {
    let imgx = 1080;
    let imgy = 2213;

    let julia_pat = julia::Julia {
        config: julia::JuliaConfig {
            x: imgx,
            y: imgy
        }
    };

    let img = julia_pat.generate();

    base64::generate_from_image(img)
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_com_example_artigen_image_ImageViewModel_generateImageJNI(
    env: JNIEnv,
    _class: JClass
) -> jstring {
    let img = generate_image();

    let s = env.new_string(img).expect("TEST");
    s.into_raw()
}