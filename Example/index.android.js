import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  BackAndroid,
  View,
} from 'react-native';

import ReactYouTube from './ReactYouTube';

export default class example extends Component {
 	componentDidMount() {
        BackAndroid.addEventListener('hardwareBackPress', this.handleBackButton);
    }

    componentWillUnmount() {
        BackAndroid.removeEventListener('hardwareBackPress', this.handleBackButton);
    }

    handleBackButton() {
        return true;
    }
    
  render() {
    return (
      <View style={styles.container}>
		<ReactYouTube 
		videoId={"sQm3djLYWB8"} // The YouTube video ID
  play={false}                                               // control playback of video with true/false
  hidden={false}                                             // control visibility of the entire view
  playsInline={true}                                         // control whether the video should play inline
  loop={false}                                               // control whether the video should loop when ended
  onChangeState={this._onNativeViewStateChange}
  ref={component => this._root = component}
  style={{alignSelf: 'stretch', height: 250, backgroundColor: 'black'}}
  apiKey={"AIzaSyAvmyh2twnfoHPoUpMwDyBZLzLJjCvqTUc"}
		/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
		backgroundColor: '#F5FCFF',
	},
	welcome: {
		fontSize: 20,
		textAlign: 'center',
		margin: 10,
	},
	instructions: {
		textAlign: 'center',
		color: '#333333',
		marginBottom: 5,
	},
	image: {
		flex: 1,
      width: 400,
      height: 400,
	}
});

AppRegistry.registerComponent('example', () => example);
