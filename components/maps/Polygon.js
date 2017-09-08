import React, {PropTypes, PureComponent} from 'react'
import {PixelRatio, Platform, requireNativeComponent, ViewPropTypes, View} from 'react-native'
import {LatLng} from '../PropTypes'

export default class Polygon extends PureComponent {
  static propTypes = {
      ...View.propTypes,

    /**
     * 节点
     */
    coordinates: PropTypes.arrayOf(LatLng).isRequired,

    /**
     * 边线宽度
     */
    strokeWidth: PropTypes.number,

    /**
     * 边线颜色
     */
    strokeColor: PropTypes.string,

    /**
     * 填充颜色
     */
    fillColor: PropTypes.string,

    /**
     * 层级
     */
    zIndex: PropTypes.number,
  }

  render() {
    const props = {
      ...this.props,
      ...Platform.select({
        android: {
          strokeWidth: PixelRatio.getPixelSizeForLayoutSize(this.props.strokeWidth),
        },
      }),
    }
    return <AMapPolygon {...props}/>
  }
}

const AMapPolygon = requireNativeComponent('AMapPolygon', Polygon)
