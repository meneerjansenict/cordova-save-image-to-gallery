//
//  imageToGallerySaverPlugin.h
//  PhoneGap/Cordova plugin to save base64 encoded image data to iOS Photos app
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Modified by Flexkids on 24/08/2018.
//
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//	MIT Licensed
//


#import <Cordova/CDVPlugin.h>

@interface imageToGallerySaverPlugin : CDVPlugin
{
	NSString* callbackId;
}

@property (nonatomic, copy) NSString* callbackId;

- (void)saveImageDataToLibrary:(CDVInvokedUrlCommand*)command;

@end
