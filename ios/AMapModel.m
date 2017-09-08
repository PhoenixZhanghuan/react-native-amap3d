#import "AMapModel.h"


@implementation AMapModel {
}

// 导出模块，不添加参数即默认为这个类名
RCT_EXPORT_MODULE();

- (MAOverlayRenderer *)renderer {
    return nil;
}


RCT_EXPORT_METHOD(calculateLineDistance:(NSDictionary *)start
                                    end:(NSDictionary *)end
                               resolver:(RCTResponseSenderBlock)callback){
    //1.将两个经纬度点转成投影点
    MAMapPoint point1 = MAMapPointForCoordinate(CLLocationCoordinate2DMake([[start objectForKey:@"latitude"] doubleValue],[[start objectForKey:@"longitude"] doubleValue]));
    MAMapPoint point2 = MAMapPointForCoordinate(CLLocationCoordinate2DMake([[end objectForKey:@"latitude"] doubleValue],[[end objectForKey:@"longitude"] doubleValue]));
    //2.计算距离
    CLLocationDistance distance = MAMetersBetweenMapPoints(point1,point2);
    NSLog(@"---------ddddddd222222222-------%f",distance);
    NSString *result = [NSString stringWithFormat:@"%f",distance]; //准备回调回去的数据
    callback(@[[NSNull null],result]);
}

//// 导出方法，桥接到js的方法返回值类型必须是void
//RCT_EXPORT_METHOD(doSomething:(NSString *)testStr resolver:(RCTResponseSenderBlock)callback){
//    NSLog(@"%@ ===> doSomething",testStr);
//    NSString *callbackData = @"Callback数据"; //准备回调回去的数据
//    callback(@[[NSNull null],callbackData]);
//}



@end
