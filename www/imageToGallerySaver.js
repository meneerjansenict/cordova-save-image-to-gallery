//
//  imageToGallerySaver.js
//  PhoneGap/Cordova plugin to save images in base64 encoding to the iOS gallery or Android gallery.
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Modified by Simba Zhang on 09/10/2015
//  Modified by Flexkids on 24/08/2018
//
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//  MIT Licensed
//

  module.exports = {

    saveImageDataToLibrary:function(successCallback, failureCallback, base64Data) {
        // successCallback required
        if (typeof successCallback != "function") {
            console.log("imageToGallerySaverPlugin Error: successCallback is not a function");
        }
        else if (typeof failureCallback != "function") {
            console.log("imageToGallerySaverPlugin Error: failureCallback is not a function");
        }
        else if (typeof base64Data != "string") {
            console.log("imageToGallerySaverPlugin Error: base64Data is not a string");
        }
        else {
            var imageData = base64Data.replace(/data:image\/png;base64,/,'');

            return cordova.exec(
              successCallback,
              failureCallback,
              "imageToGallerySaverPlugin",
              "saveImageDataToLibrary",
              [imageData]
            );
        }
    }
  };
