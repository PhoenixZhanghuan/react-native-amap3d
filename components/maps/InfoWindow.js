import {requireNativeComponent, ViewPropTypes, View} from 'react-native'

export default requireNativeComponent('AMapInfoWindow', {
  propTypes: {
      ...View.propTypes,
  }
})
