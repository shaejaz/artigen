extern crate jni;

use artigen::patterns::{julia, Pattern, blocks, from_json_str};
use artigen::output::base64;

use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::{jstring};

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_com_shaejaz_artigen_image_ImageViewModel_generateImageJNI(
    env: JNIEnv,
    _class: JClass,
    pattern: JString,
    config: JString,
) -> jstring {
    let o_pattern: String = env.get_string(pattern).expect("TEST").into();
    let o_config: String = env.get_string(config).expect("TEST").into();

    let mut img_str = String::from("");

    if o_pattern == "Blocks" {
        let config_deserilized: blocks::BlocksConfig = from_json_str(&o_config).expect("TEST");

        let blocks_pattern = blocks::Blocks {
            config: config_deserilized
        };

        let img = blocks_pattern.generate();
        img_str = base64::generate_from_image(img);
    } else if o_pattern == "Julia" {
        let config_deserilized: julia::JuliaConfig = from_json_str(&o_config).expect("TEST");

        let julia_pattern = julia::Julia {
            config: config_deserilized
        };

        let img = julia_pattern.generate();
        img_str = base64::generate_from_image(img);
    }

    let s = env.new_string(img_str).expect("TEST");
    s.into_raw()
}