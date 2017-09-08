import React, {Component} from 'react'
import {StyleSheet, Alert,NativeModules} from 'react-native'
import {MapView, Polyline} from 'react-native-amap3d'

var AMapModel = require('react-native').NativeModules.AMapModel;


export default class PolylineExample extends Component {
  static navigationOptions = {
    title: '绘制折线',
  }
 // _line1 = [
 //    {
 //      latitude: 40.006901,
 //      longitude: 116.097972,
 //    },
 //    {
 //      latitude: 40.006901,
 //      longitude: 116.597972,
 //    },
 //  ]

 //  _line2 = [
 //    {
 //      latitude: 39.906901,
 //      longitude: 116.097972,
 //    },
 //    {
 //      latitude: 39.906901,
 //      longitude: 116.597972,
 //    },
 //  ]

   _line3 = [
    {
      latitude: 31.190819,
      longitude: 121.454897,
    },
    {
      latitude: 31.183843,
      longitude: 121.457054,
    },
    {
      latitude: 31.189057,
      longitude: 121.458278,
    },
    {
      latitude: 31.191057,
      longitude: 121.483278,
    },
  ]

  constructor(props) {
      super(props);
      this.array = [];
      this.state = {
          lineArray: [],
          latitude: 39.9042,
          longitude: 116.4074,
      }
  }

  componentDidMount(){
    // AMapModule.calculateLineDistance({latitude: 39.989612, longitude: 116.480972},{latitude: 39.990347, longitude: 116.480441})
    AMapModel.calculateLineDistance({latitude: 39.989612, longitude: 116.480972},{latitude: 39.990347, longitude: 116.480441},(error,events) => {
        if (error) {
            console.warn(error);
        } else {
            alert(events) // 返回的距离 m
        }
    });
  }

  _onPress = () => Alert.alert('onPress')

  render() {
    return <MapView style={{width: 375,height: 400}} locationEnabled = {true}
                

                onLocation={({nativeEvent}) =>{

                                  console.log('ssss',nativeEvent.latitude,nativeEvent.longitude);
                                  let obj = {latitude: nativeEvent.latitude,longitude: nativeEvent.longitude}
                                  // console.log('obj --------',obj);
                                  // this.array.push(obj)
                                  this.setState({
                                    latitude: nativeEvent.latitude,
                                    longitude: nativeEvent.longitude,
                                  })

                              }
                }
                zoomLevel = {14}
                coordinate={{
                  latitude: this.state.latitude,
                  longitude: this.state.longitude,
                }}>
      
      <Polyline
        gradient
        width={5}
        colors={['#f44336', '#2196f3', '#4caf50']}
        onPress={this._onPress}
        coordinates={this._line3}/>
    </MapView>
  }
}
