#import <MAMapKit/MAOverlayRenderer.h>
#import "RCTBridgeModule.h"

@interface AMapModel : UIView <MAOverlay,RCTBridgeModule>
- (MAOverlayRenderer *)renderer;
@end
