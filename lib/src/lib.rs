extern crate jni;

use std::ffi::CString;
use std::{io::{Cursor}, vec};
use std::os::raw::c_char;
use rand::Rng;

use imageproc::integral_image::ArrayData;
use base64::{encode};

use jni::JNIEnv;
use jni::objects::{JClass, JList, JObject, JValue};
use jni::sys::{jint, jintArray, jlong, jobject, jstring};
use jni::signature::{ReturnType, Primitive};

fn generate_image() -> String {
    let imgx = 1080;
    let imgy = 2213;

    let scalex = 3.0 / imgx as f32;
    let scaley = 6.67 / imgy as f32;

    // Create a new ImgBuf with width: imgx and height: imgy
    let mut imgbuf = image::ImageBuffer::new(imgx, imgy);

    // Iterate over the coordinates and pixels of the image
    for (x, y, pixel) in imgbuf.enumerate_pixels_mut() {
        let a = (0.3 * x as f32) as u8;
        let b = (0.3 * y as f32) as u8;
        *pixel = image::Rgb([a, 0, b]);
    }

    // A redundant loop to demonstrate reading image data
    for x in 0..imgx {
        for y in 0..imgy {
            let cx = y as f32 * scalex - 1.5;
            let cy = x as f32 * scaley - 1.5;

            let c = num_complex::Complex::new(-0.4, 0.6);
            let mut z = num_complex::Complex::new(cx, cy);

            let mut i = 0;
            while i < 255 && z.norm() <= 2.0 {
                z = z * z + c;
                i += 1;
            }

            let pixel = imgbuf.get_pixel_mut(x, y);
            let image::Rgb(data) = *pixel;
            *pixel = image::Rgb([data[0], i as u8, data[2]]);
        }
    }

    let mut buf = Cursor::new(vec![]);
    imgbuf.write_to(&mut buf, image::ImageOutputFormat::Png).expect("TEST");

    encode(buf.into_inner())
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn Java_com_example_artigen_MainActivity_invokeCallbackViaJNI(
    env: JNIEnv,
    _class: JClass
) -> jstring {
    let img = generate_image();

    let s = env.new_string(img).expect("TEST");
    s.into_raw()
}