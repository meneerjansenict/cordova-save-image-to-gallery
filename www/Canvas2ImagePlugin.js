//
//  Canvas2ImagePlugin.js
//  Base64ImageSaverPlugin PhoneGap/Cordova plugin
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Modified by Simba Zhang on 09/10/2015
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//  MIT Licensed
//

  module.exports = {
    
    saveImageDataToLibrary:function(successCallback, failureCallback, base64Data) {
        // successCallback required
        if (typeof successCallback != "function") {
            console.log("Canvas2ImagePlugin Error: successCallback is not a function");
        }
        else if (typeof failureCallback != "function") {
            console.log("Canvas2ImagePlugin Error: failureCallback is not a function");
        }
        else {
            var imageData = base64Data.replace(/data:image\/png;base64,/,'');
            return cordova.exec(successCallback, failureCallback, "Canvas2ImagePlugin","saveImageDataToLibrary",[imageData]);
        }
    }
  };
  
