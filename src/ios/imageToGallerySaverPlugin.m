//
//  imageToGallerySaverPlugin.m
//  PhoneGap/Cordova plugin to save base64 image data to the iOS Photos app
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Modified by Flexkids on 24/08/2018.
//
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//	MIT Licensed
//

#import "imageToGallerySaverPlugin.h"
#import <Cordova/CDV.h>

@implementation imageToGallerySaverPlugin
@synthesize callbackId;

- (void)saveImageDataToLibrary:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSData* imageData = [[NSData alloc] initWithBase64EncodedString:[command.arguments objectAtIndex:0] options: 0];

  UIImage* image = [[[UIImage alloc] initWithData:imageData] autorelease];
  UIImageWriteToSavedPhotosAlbum(image, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
}

- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
  CDVPluginResult* result = [CDVPluginResult
                         resultWithStatus: CDVCommandStatus_OK
                         messageAsString:@"Image saved"];

  // Was there an error?
  if (error != NULL)
  {
    result = [CDVPluginResult resultWithStatus: CDVCommandStatus_ERROR messageAsString:error.description];
  }

  [self.commandDelegate sendPluginResult: result callbackId:self.callbackId];
}

- (void)dealloc
{
  [callbackId release];
  [super dealloc];
}


@end
